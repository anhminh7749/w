package com.watch.shopwatchonline.Controller.Site;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.watch.shopwatchonline.Model.Product;
import com.watch.shopwatchonline.Model.Raiting;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Model.wishlist;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.Repository.WishlistRepository;
import com.watch.shopwatchonline.Service.ProductService;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("api/user/wishlist")
public class wishlistController {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private ProductService ProductService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ModelAndView index(ModelMap model, HttpServletRequest request,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<Integer> sort) {

        Page<Product> resultPage = null;

        int curPage = page.orElse(1);
        int pageSize = size.orElse(6);
        int abx = sort.orElse(1);
        if (!sort.isEmpty()) {

            Sort sortable = Sort.by("id").ascending();

            if (abx == 1) {
                sortable = Sort.by("id").ascending();
            }

            if (abx == 3) {
                sortable = Sort.by("price").ascending();
            }

            if (abx == 4) {
                sortable = Sort.by("discount").descending();
            }

            if (abx == 2) {
                sortable = Sort.by("name").descending();
            }

            Pageable pageable = PageRequest.of(curPage - 1, pageSize, sortable);

            resultPage = ProductService.findByUserName(getUser(request), pageable);
            model.addAttribute("sort", abx);

        } else {
            Pageable pageable = PageRequest.of(curPage - 1, pageSize);

            resultPage = ProductService.findByUserName(getUser(request), pageable);

        }

        int totalPages = resultPage.getTotalPages();

        if (totalPages > 0) {
            int start = Math.max(1, curPage - 2);
            int end = Math.min(curPage + 2, totalPages);

            if (totalPages > 6) {
                if (end == totalPages) {
                    start = end - 6;
                } else {
                    if (start == 1) {
                        end = start + 6;
                    }
                }
            }

            List<Integer> pageNums = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNums", pageNums);

        }

        model.addAttribute("productPage", resultPage);
        return new ModelAndView("web-site/wishlist", model);
            }

    
    @GetMapping("/like")
    public  ResponseEntity<?> saveOrUpdateCompany(
        @RequestBody @RequestParam(name = "username", required = false) String username,
        @RequestBody @RequestParam(name = "id") Integer id) {
  
      try {
        wishlist wl = new wishlist();
        Optional<Product> opt = ProductService.findById(id);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            if (!wishlistRepository.check(opt.get().getId(), user.get().getId()).isEmpty()) {

                 wishlistRepository.deleteByProAndUser(opt.get().getId(), user.get().getId());

                return  ResponseEntity.ok().body(0);
            } else {

                wl.setProduct(opt.get());
                wl.setUsers(user.get());

                wl.setCreateAt(new Date());
                wishlistRepository.save(wl);

              
                return  ResponseEntity.ok().body(1);
            }
        } else {
            return  ResponseEntity.ok().body("null");
        }
      } catch (Exception e) {
        return  ResponseEntity.ok().body(0);
      }

    }

    public String getUser(HttpServletRequest request) {
        String token = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(token);

        return username;
    }


    //@ResponseBody get đc path 
}