package com.watch.shopwatchonline;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.javafaker.Faker;
import com.watch.shopwatchonline.Model.Blog;
import com.watch.shopwatchonline.Model.DiscountCode;
import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.BlogRepository;
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
//2076-2027
@RestController
    @RequestMapping("/random")
    public class faker {
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
        private DiscountCodeRepository codeRepository;
@Autowired
        private BlogRepository blogRepository;

      private final ObjectMapper objectMapper;
     
      public faker(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
      }
      @GetMapping("")
      public JsonNode getRandomPersons() {
     
        Faker faker = new Faker(new Locale("Vi"));
        ArrayNode persons = objectMapper.createArrayNode();
     
        for (int i = 0; i < 100; i++) {
          
          // Product pro = new Product();
          // pro.setActive((short) faker.number().numberBetween(0, 1));
          // pro.setCreateAt(faker.date().past(10,TimeUnit.DAYS));
          // pro.setDiscount(faker.number().numberBetween(0, 1000000000));
          // pro.setDescription(faker.lorem().characters());
          // pro.setName( faker.name().title());
          // pro.setPrice(faker.number().numberBetween(1000, 1000000000));
          // pro.setUpdateAt(faker.date().future(10,TimeUnit.DAYS));
          // pro.setQuantity(faker.number().numberBetween(0, 9000));
          // pro.setThumbnail(null);
          // productRepository.save(pro);
            
// DiscountCode code = new DiscountCode();
// code.setAmountApplied((float) faker.number().numberBetween(1000, 1000000000));
// code.setApplicableDate(faker.date().past(100,TimeUnit.DAYS));
// code.setByteCode(faker.finance().bic());
// code.setDescription(faker.name().title());
// code.setExpirationDate(faker.date().between(faker.date().past(100,TimeUnit.DAYS),faker.date().future(10,TimeUnit.DAYS)));
// code.setName(faker.name().title());
// code.setStatus((short) faker.number().numberBetween(0, 1));
// code.setQuantity(faker.number().numberBetween(0, 350));
// code.setReductionAmount((float) faker.number().numberBetween(1000, 1000000));
//           codeRepository.save(code);

Blog blog = new Blog();
blog.setActive((short) faker.number().numberBetween(0, 1));
blog.setCreateAt(faker.date().past(1000,TimeUnit.DAYS));
blog.setDescription(faker.lorem().characters(3));
blog.setBanner(faker.code().ean13());
blog.setProduct(productRepository.findById(faker.number().numberBetween(2027, 2076)).get());
blog.setShortdecs(faker.lorem().characters());
blog.setUpdateAt(faker.date().future(30,TimeUnit.DAYS));
blog.setTitle(faker.name().title());
blogRepository.save(blog);
        }
     
        return persons;
      }
    }