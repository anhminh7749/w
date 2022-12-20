package com.watch.shopwatchonline.Controller.Site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.watch.shopwatchonline.Controller.Admin.OrderController;
import com.watch.shopwatchonline.Domain.OrderDto;
import com.watch.shopwatchonline.Domain.ProductDto;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.Order;
import com.watch.shopwatchonline.Model.OrderDetail;
import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Model.Raiting;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.OrderDetailRepository;
import com.watch.shopwatchonline.Repository.OrderRepository;
import com.watch.shopwatchonline.Repository.ProductRepository;
import com.watch.shopwatchonline.Repository.RaitingRepository;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

@Controller
@RequestMapping("/api/user/orders")
public class OrderUserController {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository detailRepository;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private RaitingRepository raitingRepository;

  @GetMapping("")
  public String index(Model model, HttpServletRequest request) {
    model.addAttribute("orders", getTotalPriceAndQuantityWithUser(request, null));
    return "web-site/user/order-history";
  }

  @GetMapping("/find")
  public @ResponseBody List<OrderDto> view(
      @RequestBody @RequestParam(name = "status") String status, HttpServletRequest request) {
    return getTotalPriceAndQuantityWithUser(request, Short.parseShort(status));
  }

  @GetMapping("/check/raitting")
  public @ResponseBody Raiting raiting(
      @RequestBody @RequestParam(name = "id") String id) {
    Raiting raiting = new Raiting();
    Optional<OrderDetail> detail = detailRepository.findById(Integer.parseInt(id));
    if (detail.isPresent()) {
      if (detail.get().getRaitings() != null) {
        raiting = raitingRepository.findById(detail.get().getRaitings().getId()).get();
      }
    } 
    raiting.setUsers(null);
    raiting.setOrderDetails(null);
    return raiting;
  }

  @PostMapping("/create/raitting")
  public @ResponseBody void createraiting(
      @RequestBody @RequestParam(name = "id") String id, @RequestBody Raiting raitings, HttpServletRequest request) {
    Optional<User> user = userRepository.findByUsername(jwtUtils.getUser(request));
    Optional<Raiting> rai = raitingRepository.findByUserAndDetail(Integer.parseInt(id));
    Raiting raiting = new Raiting();

    if (rai.isPresent()) {
      BeanUtils.copyProperties(rai.get(), raiting);
    } else {
      raiting.setUsers(user.get());
    }
    Short x = 0;
    raiting.setPoint(raitings.getPoint());
    raiting.setComment(raitings.getComment());
    raiting.setActive(x);
    raiting.setCreateAt(new Date());
    
    raitingRepository.save(raiting);
    Optional<OrderDetail> detail = detailRepository.findById(Integer.parseInt(id));
    detail.get().setRaitings(raiting);
    detailRepository.save(detail.get());
  }

  public List<OrderDto> getTotalPriceAndQuantityWithUser(HttpServletRequest request, Short status) {
    Optional<User> user = userRepository.findByUsername(jwtUtils.getUser(request));
    List<Order> orders = null;
    if (status != null) {
      if (status == 7) {
        orders = orderRepository.FindbyUserName(user.get().getId());
      } else if (status == 6) {
        orders = orderRepository.FindbyUserNameandStatusCancel(user.get().getId());
      } else {
        orders = orderRepository.FindbyUserNameandStatus(user.get().getId(), status);
      }
    } else {
      orders = orderRepository.FindbyUserName(user.get().getId());
    }

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

  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
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

  @GetMapping("/detail/{id}")
  public String ordersdetail(@PathVariable(name = "id") int id, Model model) {
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
    model.addAttribute("detail", details);
    return "web-site/user/ordersdetail";
  }

}
