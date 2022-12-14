package com.watch.shopwatchonline.Controller.Admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.watch.shopwatchonline.Domain.MailDto;
import com.watch.shopwatchonline.Model.Mail;
import com.watch.shopwatchonline.Repository.MailRepository;
import com.watch.shopwatchonline.Service.MailService;

@Controller
@RequestMapping("api/admin/contacs")
public class ContactController {
	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	MailRepository mailRepository;
	
	@Autowired
	MailService mailService;

	@GetMapping("/edit/{mailId}")
	public ModelAndView edit(ModelMap model, @PathVariable("mailId") Integer mailId) {

		Optional<Mail> opt = mailRepository.findById(mailId);
		MailDto dto = new MailDto();

		if (opt.isPresent()) {
			Mail entity = opt.get();

			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);

			model.addAttribute("mail", dto);

			return new ModelAndView("web-admin/mail", model);
		}

		model.addAttribute("message", "Mail is existed");

		return new ModelAndView("forward:/api/admin/contacs", model);
	}

	   @GetMapping("/delete")
	    public @ResponseBody ModelAndView delete(@RequestParam(name = "id") String id) {
	        mailRepository.deleteById(Integer.parseInt(id));
	        return new ModelAndView("forward:/api/admin/contacs");
	    }
	   
	@PostMapping("/send")
	public ModelAndView sendMail(ModelMap model,@Valid @ModelAttribute("mail") MailDto dto,
			@RequestParam("gmail") String to,
			@RequestParam("title") String subject,
			@RequestParam("description") String content,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("web-admin/mail");
		}
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(content);
		javaMailSender.send(msg);

		Mail entity = mailRepository.findById(dto.getMailId()).get();
		// admin
		entity.setStatus((short) 1);
		// 0 = chua phan hoi short
		// 1= da phan hoi
		// Date date = new Date();
		// entity.setCreateAt(date);
		// entity.setUpdateAt(date);
		Date now = new Date();

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(dateFormat.format(new Date()));
			// System.out.println("date: "+date);
			// System.out.println("--------------------------------------------");
			entity.setUpdateAt(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(entity);

		mailRepository.save(entity);
		model.addAttribute("message", "Mail is saved!");
		return new ModelAndView("redirect:/api/admin/contacs", model);
	}

	@GetMapping("")
	public String list(ModelMap model) {
		List<Mail> list = mailRepository.findAll();

		model.addAttribute("mails", list);
		return "web-admin/listmail";
	}
}
