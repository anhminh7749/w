package com.watch.shopwatchonline.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.watch.shopwatchonline.Domain.AddressDto;
import com.watch.shopwatchonline.Domain.OrderDto;
import com.watch.shopwatchonline.Domain.ProductDto;
import com.watch.shopwatchonline.Domain.UserDto;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.ChatBox;
import com.watch.shopwatchonline.Model.DiscountCode;
import com.watch.shopwatchonline.Model.Order;
import com.watch.shopwatchonline.Model.OrderDetail;
import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.ChatBoxRepository;
import com.watch.shopwatchonline.Repository.DiscountCodeRepository;
import com.watch.shopwatchonline.Repository.OrderDetailRepository;
import com.watch.shopwatchonline.Repository.OrderRepository;
import com.watch.shopwatchonline.Repository.ProductRepository;
import com.watch.shopwatchonline.Repository.RaitingRepository;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.Service.StogareService;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api")
public class HomeController {

  @Autowired
  private StogareService StogareService;
  @Autowired
  private ChatBoxRepository boxRepository;
  @Autowired
  private DiscountCodeRepository discountCodeRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private JwtUtils utils;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderDetailRepository detailRepository;
  @Autowired
  private ProductRepository productRepository;


  @GetMapping("/abc")
  public String alless() {
    return "web-site/user/tete";
  }
  @GetMapping("/auth/login")
  public String allAccess() {
    return "web-admin/login";
  }

  @GetMapping("/admin")
  public String main() {
    return "web-admin/index";
  }
  @GetMapping("/site")
  public String viewmain() {
    return "web-site/main";
  }

  @GetMapping("/site/abc")
  public String profileUser() {
    return "web-site/user/profile";
  }

  @GetMapping("/auth/site/login")
  public String all() {
    return "web-site/login";
  }

  @GetMapping("/auth/register")
  public String register() {
    return "web-admin/signup";
  }

  @GetMapping("/auth/site/register")
  public String siteRegister() {
    return "web-site/signup";
  }

  @GetMapping("/address")
  public String add() {
    return "web-site/address";
  }

  

  @GetMapping("/site/user/history-order/detail/{id}")
  public String aa(@PathVariable(name = "id") int id, Model model) {
    List<OrderDetail> details = detailRepository.FindByOrder(id);
    List<ProductDto> dtos = new ArrayList<>();
    for (OrderDetail detail : details) {
      ProductDto dto = new ProductDto();
      Optional<Product> pro = productRepository.findById(detail.getProduct().getId());
      BeanUtils.copyProperties(pro.get(), dto);
      dto.setPrice(detail.getPrice());
      dto.setQuantity(detail.getQuantity());
      dto.setDiscount(detail.getDiscount());
      dtos.add(dto);
    }
    model.addAttribute("detail", dtos);
    return "web-site/shoppingdetail";
  }

  @RequestMapping(value = "/site/user/order/cancel", method = RequestMethod.POST)
  public @ResponseBody ResponseEntity<?> CancelOrder(@RequestParam(name = "id") String id) {
    Optional<Order> orders = orderRepository.findById(Integer.parseInt(id));
    if (!orders.isEmpty()) {
      Order order = new Order();
      BeanUtils.copyProperties(orders.get(), order);
      order.setStatus((short) 4);
      order.setUpdateAt(new Date());
      order.setCancellationDate(new Date());
      orderRepository.save(order);
    } else {
      return ResponseEntity.ok().body("isEmpty");
    }

    return ResponseEntity.ok().body("success");
  }

  @GetMapping("/site/user")
  public String ShoppingCartDetail(Model model, HttpServletRequest request) {

    model.addAttribute("orders", getTotalPriceAndQuantityWithUser(request));
    return "web-site/profile";
  }

  public List<OrderDto> getTotalPriceAndQuantityWithUser(HttpServletRequest request) {

    Optional<User> user = userRepository.findByUsername(utils.getUser(request));
    List<Order> orders = orderRepository.FindbyUserName(user.get().getId());
    List<OrderDto> ordersDto = new ArrayList<>();
    for (Order order : orders) {
      OrderDto dto = new OrderDto();
      BeanUtils.copyProperties(order, dto);
      float total = 0;
      int quantity = 0;
      List<OrderDetail> details = detailRepository.FindByOrder(order.getId());
      for (OrderDetail detail : details) {
        total += (detail.getPrice() - detail.getDiscount()) * detail.getQuantity();
        quantity += detail.getQuantity();
        dto.setTotalAmount(total);
        dto.setTotalQuantity(quantity);
      }
      ordersDto.add(dto);
    }
    return ordersDto;
  }

  @GetMapping("/chatbox")
  public String chatbox(Model model, HttpServletRequest request) {
    try {
      List<ChatBox> list = boxRepository.findAll();
      model.addAttribute("listChatBox", list);
    } catch (Exception e) {
      System.out.println(e);
    }

    return "web-admin/compose";
  }

  @PostMapping("/address/up")
  public String upadd(ModelMap model, @ModelAttribute("address") AddressDto dto, HttpServletRequest request) {

    Address address = new Address();
    BeanUtils.copyProperties(dto, address);
    Optional<User> user = userRepository.findByUsername(utils.getUser(request));
    address.setUsers(user.get());

    if (dto.getStatus().intValue() == 1) {
      Address add = addressRepository.findByStatus((short) 1);
      if (add != null) {
        add.setStatus((short) 0);
        addressRepository.save(add);
      }
    }
    addressRepository.save(address);
    System.out.println("------------------------------------------------");
    String rr = "redirect:" + dto.getRedirect();
    return rr;
  }

  @GetMapping("ShoppingCart")
  public String ShoppingCart(Model model, HttpServletRequest request, HttpServletResponse response) {
    Optional<User> user = userRepository.findByUsername(utils.getUser(request));
    List<Address> address = addressRepository.findbyuser(user.get().getId());
    if (!address.isEmpty()) {
      model.addAttribute("listaddress", address);
      System.out.println("------------------------------------------------");

    }
    return "web-site/checkout";
  }

  @GetMapping("/register")
  public String Access() {
    return "web-admin/signup";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("images/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serverFile(@PathVariable(name = "filename") String fileName) {
    Resource file = StogareService.loadResource(fileName);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

}