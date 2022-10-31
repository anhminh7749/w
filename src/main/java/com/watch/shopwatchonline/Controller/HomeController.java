package com.watch.shopwatchonline.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.watch.shopwatchonline.Service.StogareService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api")
public class HomeController {

  @Autowired
   private StogareService StogareService;

    @GetMapping("/login")
    public String allAccess() {
      return "web-admin/login";
    }
    @GetMapping("/site/login")
    public String all() {
      return "web-site/login";
    }
    @GetMapping("/ShoppingCart")
    public String ShoppingCart() {
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
    public ResponseEntity < Resource > serverFile(@PathVariable(name = "filename") String fileName) {
      Resource file = StogareService.loadResource(fileName);
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}
