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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.watch.shopwatchonline.Domain.AddressDto;
import com.watch.shopwatchonline.Domain.BrandDto;
import com.watch.shopwatchonline.Domain.OrderDto;
import com.watch.shopwatchonline.Domain.ProductDto;
import com.watch.shopwatchonline.Domain.Statistics;
import com.watch.shopwatchonline.Domain.UserDto;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.Brand;
import com.watch.shopwatchonline.Model.ChatBox;
import com.watch.shopwatchonline.Model.DiscountCode;
import com.watch.shopwatchonline.Model.Order;
import com.watch.shopwatchonline.Model.OrderDetail;
import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.BrandRepository;
import com.watch.shopwatchonline.Repository.ChatBoxRepository;
import com.watch.shopwatchonline.Repository.DiscountCodeRepository;
import com.watch.shopwatchonline.Repository.OrderDetailRepository;
import com.watch.shopwatchonline.Repository.OrderRepository;
import com.watch.shopwatchonline.Repository.ProductRepository;
import com.watch.shopwatchonline.Repository.RaitingRepository;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.Repository.WishlistRepository;
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
  private WishlistRepository wishlistRepository;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private JwtUtils utils;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RaitingRepository raitingRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderDetailRepository detailRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private BrandRepository brandRepository;

  @GetMapping("/auth/login")
  public String allAccess() {
    return "web-admin/login";
  }

  @GetMapping("/abcabc")
  public String allAccs() {
    return "web-site/index1";
  }

  @GetMapping("/site/brands")
  public String brands(Model model, @RequestParam("page") Optional<Integer> page) {
    int pages = page.orElse(1);
    Pageable pageable = PageRequest.of(pages, 6);
    List<Brand> brands = brandRepository.findAll();
    List<BrandDto> cnst = new ArrayList<>();
    for (Brand brand : brands) {
      BrandDto brandDto = new BrandDto();
      BeanUtils.copyProperties(brand, brandDto);
      brandDto.setCountBrandProduct(productRepository.countBrandProduct(brand.getId()));
      cnst.add(brandDto);
      System.out.println(brandDto);
    }

    model.addAttribute("brands", cnst);
    return "web-site/brands";
  }

  @GetMapping("/admin/staticyear")
  public @ResponseBody List<Statistics> chart() {
    List<String> s = orderRepository.statisticyear();
    String[] words = null;
    List<Statistics> list = new ArrayList<>();
    for (int i = 0; i < s.size(); i++) {
      Statistics statistics = new Statistics();

      words = s.get(i).split("\\,");

      statistics.setStartyear(words[0]);
      statistics.setValue(words[1]);

      list.add(statistics);
    }
    return list;
  }


  @GetMapping("/admin")
  public String main(Model model) {
    // List<Statistics> list = new ArrayList<>();
    // for (Statistics statistics : orderRepository.statistics()) {
    // System.out.println(statistics);
    // }
List<Integer> rai = new ArrayList<>();
    for (int i = 1; i < 6; i++) {
      rai.add(raitingRepository.avgRaittingforPoint(i));
    }
   // model.addAttribute("countpoint",raitingRepository.findByUserAndPoint());
    model.addAttribute("countpointuser",rai);
    model.addAttribute("countUsers", userRepository.countUsers());
    model.addAttribute("countOrders30", orderRepository.countOrders30());
    model.addAttribute("sumStockProduct", productRepository.sumStockProduct());
    model.addAttribute("countOrdersDetail30", detailRepository.countOrdersDetail30());
    model.addAttribute("countWishList", wishlistRepository.countWishList());
    model.addAttribute("avgRaitting", raitingRepository.avgRaitting());
    model.addAttribute("countOrders", orderRepository.countOrders());

    return "web-admin/index";
  }

  @GetMapping("/site")
  public String viewmain() {
    return "web-site/main";
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

  @GetMapping("/static")
  public ResponseEntity<?> pro(
      @RequestParam(name = "startday", required = false) String startday,
      @RequestParam(name = "startmonth", required = false) String startmonth,
      @RequestParam(name = "startyear", required = false) String startyear,
      @RequestParam(name = "endday", required = false) String endday,
      @RequestParam(name = "endmonth", required = false) String endmonth,
      @RequestParam(name = "endyear", required = false) String endyear,
      @RequestParam(name = "value") String value) {
    // String querydate = null;
    // String queryvalue = null;
    // String querykeyword = null;
    // String querygroup = null;
    // int chose = Integer.parseInt(value);
    // switch (chose) {
    // case 1:
    // queryvalue=" count(o.id) ";
    // break;
    // default:
    // queryvalue=" sum((od.price - od.discount)*od.quantity) ";
    // break;
    // }
    // if(endyear != null){

    // }else{
    // if(startday != null){

    // }else{
    // if(startmonth != null){

    // }else{
    // querydate = " Year(o.create_at) as startyear ";
    // querygroup = " Year(o.create_at) ";
    // querykeyword = " Year(o.create_at) >= Year(getdate()) - 5 and
    // Year(o.create_at) <= Year(getdate()) + 5 ";
    // }
    // }

    // }

    // List<Statistics> statistics = new ArrayList<>();
    // String list = orderRepository.statisticyear();

    // String[] splits = list.split(",");
    // Statistics s = new Statistics();
    // for (String item : splits) {

    // s.setStartyear(item);
    // statistics.add(s);
    // System.out.print(item);
    // System.out.println(s);
    // }

    return ResponseEntity.ok().body(null);
  }

 
  @GetMapping("/product/getInfo")
  public @ResponseBody Product getProduct(@RequestBody @RequestParam(name = "id") int id) {
    Optional<Product> product = productRepository.findById(id);
    product.get().setBrand(null);
    product.get().setCategory(null);
    product.get().setWishlists(null);
    product.get().setBlogs(null);
    product.get().setOrderDetails(null);
    return product.get();
  }

  // public List<OrderDto> getTotalPriceAndQuantityWithUser(HttpServletRequest
  // request) {

  // Optional<User> user = userRepository.findByUsername(utils.getUser(request));
  // List<Order> orders = orderRepository.FindbyUserName(user.get().getId());
  // List<OrderDto> ordersDto = new ArrayList<>();
  // for (Order order : orders) {
  // OrderDto dto = new OrderDto();
  // BeanUtils.copyProperties(order, dto);
  // float total = 0;
  // int quantity = 0;
  // List<OrderDetail> details = detailRepository.FindByOrder(order.getId());
  // for (OrderDetail detail : details) {
  // total += (detail.getPrice() - detail.getDiscount()) * detail.getQuantity();
  // quantity += detail.getQuantity();
  // dto.setTotalAmount(total);
  // dto.setTotalQuantity(quantity);
  // }
  // ordersDto.add(dto);
  // }
  // return ordersDto;
  // }

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
    System.out.println(address);
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
    String rr = "redirect:" + dto.getRedirect();
    return rr;
  }

  @GetMapping("ShoppingCart")
  public String ShoppingCart(Model model, HttpServletRequest request, HttpServletResponse response) {
    List<Address> address = new ArrayList<>();
    if (utils.getUser(request) != null) {
      Optional<User> user = userRepository.findByUsername(utils.getUser(request));
      address = addressRepository.findbyuser(user.get().getId());
    }
    model.addAttribute("listaddress", address);

    return "web-site/checkout";
  }

  @GetMapping("/images/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serverFile(@PathVariable(name = "filename") String fileName) {
    Resource file = StogareService.loadResource(fileName);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

}