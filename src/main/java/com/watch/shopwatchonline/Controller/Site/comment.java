package com.watch.shopwatchonline.Controller.Site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("site/cmt") 
public class comment {
    
    @GetMapping
     public ModelAndView detailProduct() {
      

        return new ModelAndView("web-site/comment");
    }

}
