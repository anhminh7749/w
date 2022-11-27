package com.watch.shopwatchonline.Controller.Site;

import com.watch.shopwatchonline.Domain.MailRequest;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.Service.MailService;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder encoder;
	private final MailService mailService;

	@GetMapping("/profile/info")
    public String getInfo() {
      return "web-site/index";
    }
	
	@GetMapping("/profile/change-password")
    public String openFormChangePassword(Model model) {
      return "web-site/changepassword";
    }
	
	@PostMapping("/profile/change-password")
    public String changePassword(
    		Model model,
    			@RequestParam(name = "currentPassword") String currentPassword,
    		   @RequestParam(name = "newPassword") String newPassword,
    		   @RequestParam(name = "confirmPassword") String confirmPassword,
    		   @CookieValue("shopwatch") String token
		   ) {
	log.info("Change password, request currentPassword: {}, newPassword: {}, confirmPassword: {}", currentPassword, newPassword, confirmPassword);
	model.addAttribute("tuandt", "Tuandt varrrr");
	String error = null;
	try {
		String username = jwtUtils.getUserNameFromJwtToken(token);
		User user = userRepository.findByUsername(username).orElse(null);
		if (!encoder.matches(currentPassword,  user.getPassword())) {
			error = "Mật khẩu hiện tại không đúng.";
		}
		if (!newPassword.equals(confirmPassword)) {
			error = "Mật khẩu xác nhận không trùng khớp.";
		}
		log.info("Change password error: {}", error);
		if (error == null) {
			user.setPassword(encoder.encode(newPassword));
			userRepository.save(user);
			log.info("Change password successfully");
			return "redirect:/"; 
		} else {
			log.info("Change password fail");
			model.addAttribute("error", error);
		}
	} catch (Exception e) {
		log.info("Change password exception");
		model.addAttribute("error", "Đổi mật khẩu không thành công!");
	}
	return "web-site/changepassword";
    }
	
	
	@GetMapping("/forgot-password")
	public String openFormForgotPassword() {
		return "web-site/forgotpassword";
	}
	
	@PostMapping("/forgot-password")
	public String openFormForgotPassword(@RequestParam(name = "username") String username) {
		log.info("Username: {}", username);
		User user = userRepository.findByUsername(username).orElse(null);
		if (user != null) {
			Map<String, String> params = new HashMap<>();
			String newPassword = RandomStringUtils.randomAlphabetic(10);
			log.info("Mat khau moi: {}", newPassword);
			params.put("password", newPassword);
			user.setPassword(encoder.encode(newPassword));
			userRepository.save(user);
			log.info("Luu mat khau moi cua user");
			mailService.send(MailRequest.builder()
					.to(user.getEmail())
					.subject("Thông báo lấy lại mật khẩu")
					.body("Mật khẩu mới của bạn là: [({$password})]")
					.params(params)
					.build());
			log.info("Gui email thanh cong");
			return "redirect:/api/auth/site/login";
		}
		return "redirect:/forgot-password";
	}
}