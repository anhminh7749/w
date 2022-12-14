package com.watch.shopwatchonline.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import com.watch.shopwatchonline.Domain.ProductDto;
import com.watch.shopwatchonline.Domain.UserDto;
import com.watch.shopwatchonline.Model.Erole;
import com.watch.shopwatchonline.Model.Role;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.RoleRepository;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.message.MessageResponse;
import com.watch.shopwatchonline.security.Request.LoginRequest;
import com.watch.shopwatchonline.security.Request.SignupRequest;
import com.watch.shopwatchonline.security.Response.UserInfoResponse;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping(value = "/signin")
  public @ResponseBody String authenticateUser(@RequestBody @RequestParam(name = "username") String username,
  @RequestBody @RequestParam(name = "password") String password, HttpServletResponse response) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDto userDetails = (UserDto) authentication.getPrincipal();

     ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
     System.out.println(jwtCookie);
     log.info("tvtv, jwtCookie: {}", jwtCookie.toString());
     response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString()); // ????nh jwt v??o
    String pathToRedirect = "/api/site";
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    for (String role : roles) {
      if (role.equals("ROLE_ADMIN")) {
        pathToRedirect = "/api/admin";
      }
    }
    log.info("tvtv, pathToRedirect: {}, roles: {}", pathToRedirect, roles);
    return pathToRedirect;
  }

  @PostMapping(value = "/signup")
  public String registerUser(@Validated @ModelAttribute("signUpRequest") SignupRequest signUpRequest) {
    User user = new User();
    BeanUtils.copyProperties(signUpRequest, user);
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    Role userRole = roleRepository.findByName(Erole.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    Set<Role> roles = new HashSet<>();
    roles.add(userRole);
    user.setRoles(roles);
    userRepository.save(user);
    return "redirect:/api/auth/site/login";

  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser(HttpServletResponse response) {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    return ResponseEntity.ok().body(null);
  }
}