package com.watch.shopwatchonline.Controller.Site;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.watch.shopwatchonline.Domain.MailRequest;
import com.watch.shopwatchonline.Model.Address;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Repository.AddressRepository;
import com.watch.shopwatchonline.Repository.UserRepository;
import com.watch.shopwatchonline.Service.MailService;
import com.watch.shopwatchonline.Service.StogareService;
import com.watch.shopwatchonline.security.Request.SignupRequest;
import com.watch.shopwatchonline.security.jwt.JwtUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;
	private final PasswordEncoder encoder;
	private final MailService mailService;
	@Autowired
	private StogareService stogareService;
	@Autowired
	private AddressRepository addressRepository;

	@GetMapping("/profile/info")
	public String getInfo(Model model, HttpServletRequest request) {
		Optional<User> user = userRepository.findByUsername(jwtUtils.getUser(request));
		model.addAttribute("user", user.get());
		return "web-site/user/info-user";
	}

	@GetMapping("/profile/listaddress")
	public String getaddress(Model model, HttpServletRequest request) {
		Optional<User> user = userRepository.findByUsername(jwtUtils.getUser(request));
		List<Address> addresses = addressRepository.findbyuser(user.get().getId());
		model.addAttribute("addresses", addresses);
		return "web-site/user/address-user";
	}

	@GetMapping("/profile/address")
	public ResponseEntity<?> getOneAddress(@RequestBody @RequestParam(name = "id", required = false) int id) throws JsonProcessingException {
		Optional<Address> addresses = addressRepository.findById(id);
		ObjectMapper mapper = new ObjectMapper();
		addresses.get().setUsers(null);
		addresses.get().setOrders(null);
		return ResponseEntity.ok().body((addresses.get()));
	}

	@GetMapping("/profile/address/status")
	public @ResponseBody void setStatusAddress(@RequestBody @RequestParam(name = "id", required = false) int id) {
		Address add = addressRepository.findByStatus((short) 1);
		if (add != null) {
			add.setStatus((short) 0);
			addressRepository.save(add);
		}
		Address address = addressRepository.findById(id).get();
		address.setStatus((short) 1);
		addressRepository.save(address);
	}

	@PostMapping("/profile/address/delete")
	public @ResponseBody void deleteAddress(@RequestParam(name = "id", required = false) String id) {
		try {

			Address address = addressRepository.findById(Integer.parseInt(id)).get();
			address.setDeleteAt(new Date());
			addressRepository.save(address);
		} catch (Exception e) {
			e.getMessage();
		}
		// return ResponseEntity.ok().body(true);
	}

	@GetMapping("/profile/info/check")
	public ResponseEntity<?> getInfo(@RequestBody @RequestParam(name = "email", required = false) String email,
			@RequestBody @RequestParam(name = "phone", required = false) String phone, HttpServletRequest request) {
		Optional<User> users = userRepository.findByUsername(jwtUtils.getUser(request));
		if (email != null) {
			Optional<User> user = userRepository.findByEmail(email, users.get().getId());
			if (user.isPresent()) {
				return ResponseEntity.ok().body(false);
			}
		}
		if (phone != null) {
			Optional<User> user = userRepository.findByPhone(phone, users.get().getId());
			if (user.isPresent()) {
				return ResponseEntity.ok().body(false);
			}
		}

		return ResponseEntity.ok().body(true);
	}

	@PostMapping("/profile/info/update")
	public String changeInfo(@Validated @ModelAttribute("signUpRequest") SignupRequest signUpRequest,
			BindingResult result, HttpServletRequest request) {
		User user = new User();
		Optional<User> users = userRepository.findByUsername(jwtUtils.getUser(request));
		BeanUtils.copyProperties(users.get(), user);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "redirect:/api/user/profile/info";
		}

		if (!signUpRequest.getImageFile().isEmpty()) {
			user.setAvatar(stogareService.getFileName(signUpRequest.getImageFile()));
			stogareService.store(signUpRequest.getImageFile(), user.getAvatar());
		}
		user.setBirtday(signUpRequest.getBirtday());
		user.setEmail(signUpRequest.getEmail());
		user.setGender(signUpRequest.getGender());
		user.setPhone(signUpRequest.getPhone());
		user.setName(signUpRequest.getName());
		userRepository.save(user);
		return "redirect:/api/user/profile/info";
	}

	@PostMapping("/get/img")
	public ResponseEntity<?> getimg(HttpServletRequest request) {
		Optional<User> users = userRepository.findByUsername(jwtUtils.getUser(request));
		User user = new User();
		user.setName(users.get().getName());
		user.setAvatar(users.get().getAvatar());
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/profile/change-password")
	public String openFormChangePassword(Model model) {
		return "web-site/user/changePass";
	}

	@PostMapping("/profile/change-password")
	public String changePassword(
			Model model,
			@RequestParam(name = "currentPassword") String currentPassword,
			@RequestParam(name = "newPassword") String newPassword,
			@RequestParam(name = "confirmPassword") String confirmPassword,
			@CookieValue("shopwatch") String token) {
		log.info("Change password, request currentPassword: {}, newPassword: {}, confirmPassword: {}", currentPassword,
				newPassword, confirmPassword);
		model.addAttribute("tuandt", "Tuandt varrrr");
		String error = null;
		try {
			String username = jwtUtils.getUserNameFromJwtToken(token);
			User user = userRepository.findByUsername(username).orElse(null);
			if (!encoder.matches(currentPassword, user.getPassword())) {
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
				return "redirect:/api/user/profile/change-password";
			} else {
				log.info("Change password fail");
				System.out.println(error);
				model.addAttribute("error", error);
			}
		} catch (Exception e) {
			log.info("Change password exception");
			model.addAttribute("error", "Đổi mật khẩu không thành công!");
		}
		return "web-site/user/changePass";
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
		return "redirect:/api/user/forgot-password";
	}

}
