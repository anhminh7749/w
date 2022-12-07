package com.watch.shopwatchonline.Controller.Site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("site/user")
public class UserController {
	@GetMapping("index")
	public String index() {
		return "web-site/index1";
	}
}
