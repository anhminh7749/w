package com.watch.shopwatchonline.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.watch.shopwatchonline.Domain.Cart;
import com.watch.shopwatchonline.Domain.ProductDto;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.DiscountCode;
import com.watch.shopwatchonline.Model.Order;
import com.watch.shopwatchonline.Model.OrderDetail;
import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.DiscountCodeRepository;
import com.watch.shopwatchonline.Repository.OrderDetailRepository;
import com.watch.shopwatchonline.Repository.OrderRepository;
import com.watch.shopwatchonline.Service.ProductService;
import com.watch.shopwatchonline.message.MessageResponse;

import groovyjarjarantlr4.v4.codegen.model.ExceptionClause;
import javassist.expr.NewArray;

@Controller
public class getLocalStorage {
  @Autowired
  private ProductService ProductService;
  @Autowired
  private DiscountCodeRepository discountCodeRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository orderDetailRepository;

  @RequestMapping(value = "/api/filter", method = RequestMethod.GET)
  public ResponseEntity<?> filter(
      HttpServletRequest request, HttpServletResponse response)
      throws ConcurrentModificationException, UnsupportedEncodingException {

    ObjectMapper mapper = new ObjectMapper();
    List<Cart> listcart = new ArrayList<>();
    String json = request.getHeader("shoppingcart");

    try {
      if (json != null) {
        List<Product> listrequest = mapper.readValue(URLDecoder.decode(request.getHeader("shoppingcart"), "UTF-8"),
            mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
        List<Product> list = new ArrayList<>();
        for (Product dto : listrequest) {
          int quantity = dto.getQuantity();
          Optional<Product> product = ProductService.findById(dto.getId());
          product.get().setQuantity(quantity);
          list.add(product.get());
        }

        for (Product product : list) {
          Cart cart = new Cart();
          BeanUtils.copyProperties(product, cart);
          listcart.add(cart);

        }
      
        json = UriUtils.encode(mapper.writeValueAsString(listcart), "UTF-8");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return ResponseEntity.ok().header("Update-Shopping-Cart", json).build();
  }

  @RequestMapping(value = "/cart/save", method = RequestMethod.POST)
  public ResponseEntity<?> saveOrUpdateCompany(
      @RequestBody String cart,
      @RequestParam(name = "discountcode", required = false) String code,
      @RequestParam(name = "addressId") Integer addressId) {

    try {
      Order order = new Order();
      DiscountCode discountcode = discountCodeRepository.findByByteCode(code);
      Optional<Address> address = addressRepository.findById(addressId);
      Set<OrderDetail> orderDetails = new HashSet<>();
      ObjectMapper mapper = new ObjectMapper();

      String shopcart = "";
      String[] splits = cart.split("shopcart=");
      for (String item : splits) {
        shopcart = item;
      }
      List<Product> listrequest = mapper.readValue(URLDecoder.decode(shopcart, "UTF-8"),
          mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

      if (discountcode != null) {
        order.setDiscountCode(discountcode);
        discountcode.setQuantity(discountcode.getQuantity() - 1);
        discountCodeRepository.save(discountcode);
      }

      order.setAddress(address.get());
      order.setCreateAt(new Date());
      order.setStatus((short) 0);
      /*
       * 0:chot duyet
       * 1:da duyet và đg chuển bị đơn hàng
       * 2:đang ship
       * 3: hoan thanh
       * 4: huy boi khach hang
       * 5: huy boi admin
       */
      orderRepository.save(order);
      
      for (Product product : listrequest) {
        Optional<Product> pro = ProductService.findById(product.getId());
        OrderDetail detail = new OrderDetail();
        detail.setPrice(pro.get().getPrice());
        detail.setDiscount(pro.get().getDiscount());
        detail.setQuantity(product.getQuantity());
        detail.setProduct(pro.get());
        orderDetails.add(detail);
        detail.setOrders(order);
        orderDetailRepository.save(detail);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return  ResponseEntity.ok().body("success");
  }

  @RequestMapping(value = "/code/check", method = RequestMethod.POST)
  @ResponseBody
  public String checkdiscout(@RequestBody @RequestParam(name = "discountcode", required = false) String code,
      @RequestParam(name = "total", required = false) String total) throws NumberFormatException, ParseException {
    if (code != null) {
      DiscountCode discountcode = discountCodeRepository.findByByteCode(code);
      if (discountcode != null) {
        if (code.equalsIgnoreCase(discountcode.getByteCode())) {
          if (discountcode.getQuantity() > 0) {
            if (discountcode.getAmountApplied() <= Float.parseFloat(total)) {
              if (compareDates(discountcode.getApplicableDate(), discountcode.getExpirationDate())) {
                return "" + discountcode.getReductionAmount();
              } else {
                return "date";
              }
            } else {
              return "Insufficientfunds";// k du tien
            }
          } else {
            return "oof";// out off stock
          }
        } else {
          return "mismatched";// k trung khop
        }
      } else {
        return "notexist";// code k tt
      }
    }
    return "null";// ok
  }

  private Boolean compareDates(Date star, Date end) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    String now = sdf.format(new Date());
    Date nowdate = sdf.parse(now);
    Calendar calnow = Calendar.getInstance();
    Calendar calstar = Calendar.getInstance();
    Calendar calend = Calendar.getInstance();
    calnow.setTime(nowdate);
    calstar.setTime(star);
    calend.setTime(end);
    // compare two dates
    if ((calnow.after(calstar) || calnow.equals(calstar)) && (calnow.before(calend) || calnow.equals(calend))) {
      return true;
    } else {
      return false;
    }
  }

}