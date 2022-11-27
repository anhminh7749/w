package com.watch.shopwatchonline.Controller.Admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.watch.shopwatchonline.Domain.OrderDto;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.DiscountCode;
import com.watch.shopwatchonline.Model.Order;
import com.watch.shopwatchonline.Model.OrderDetail;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.DiscountCodeRepository;
import com.watch.shopwatchonline.Repository.OrderDetailRepository;
import com.watch.shopwatchonline.Repository.OrderRepository;
import com.watch.shopwatchonline.Repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/order")
public class OrderController {
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository detailRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private DiscountCodeRepository codeRepository;
  private ObjectMapper mapper = new ObjectMapper();

  @GetMapping("/confirm")
  public @ResponseBody void confirmorder(
      @RequestParam(name = "id") String id) {
    Optional<Order> orders = orderRepository.findById(Integer.parseInt(id));
    Order order = new Order();
    BeanUtils.copyProperties(orders.get(), order);
    order.setStatus((short) (orders.get().getStatus() + 1));
    order.setUpdateAt(new Date());
    order.setCompleteDate(new Date());
    orderRepository.save(order);
  }

  @GetMapping("/cancel")
  public @ResponseBody void cancelorder(
      @RequestParam(name = "id") String id) {
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
    System.out.println(id);
    Optional<Order> orders = orderRepository.findById(Integer.parseInt(id));
    Order order = new Order();
    if (orders.isPresent()) {
      BeanUtils.copyProperties(orders.get(), order);
      order.setCancellationDate(new Date());
      order.setUpdateAt(new Date());
      order.setStatus((short) 5);
      orderRepository.save(order);
    }

  }

  @GetMapping("/show")
  public @ResponseBody void showorder(
      @RequestParam(name = "id") String id) {
    Optional<Order> orders = orderRepository.findById(Integer.parseInt(id));

  }

  @PostMapping("/delete")
  public @ResponseBody void deleteorder(
      @RequestParam(name = "id") String id) {
    Optional<Order> orders = orderRepository.findById(Integer.parseInt(id));
    Order order = new Order();
    BeanUtils.copyProperties(orders, order);
    order.setDeleteAt(new Date());
    orderRepository.save(order);
  }

  @GetMapping("")
  public String view_S() {
    return "/web-admin/ListOrder";
  }

  @GetMapping("/find")
  public @ResponseBody List<OrderDto> view(
      @RequestParam(name = "status") String status) {
    String json = new Gson().toJson(getTotalPriceAndQuantityWithStatus(Short.valueOf("7")));

    return getTotalPriceAndQuantityWithStatus(Short.valueOf(status));
  }

  public List<OrderDto> getTotalPriceAndQuantityWithStatus(Short status) {
    List<Order> orders = new ArrayList<>();
    if (status == 7) {
      orders = orderRepository.findAll();
    } else if (status == 6) {
      orders = orderRepository.findByStatusCancel();

    } else {
      orders = orderRepository.findByStatus(status);
    }

    List<OrderDto> ordersDto = new ArrayList<>();
    for (Order order : orders) {
      Optional<Address> address = addressRepository.findById(order.getAddress().getId());
      OrderDto dto = new OrderDto();
      BeanUtils.copyProperties(order, dto);
      float total = 0;
      int quantity = 0;
      float discount = 0;
      List<OrderDetail> details = detailRepository.FindByOrder(order.getId());
      for (OrderDetail detail : details) {
        total += (detail.getPrice() - detail.getDiscount()) * detail.getQuantity();
        quantity += detail.getQuantity();
        discount += detail.getDiscount();
        dto.setTotaldiscount(discount);
        dto.setTotalAmount(total);
        dto.setTotalQuantity(quantity);
        dto.setUsername(address.get().getName());
      }
      ordersDto.add(dto);
    }
    return ordersDto;
  }
}