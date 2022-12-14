package com.watch.shopwatchonline.Controller.Admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import com.watch.shopwatchonline.Domain.CustomerDto;
import com.watch.shopwatchonline.Model.Customer;
import com.watch.shopwatchonline.Model.User;
import com.watch.shopwatchonline.Service.CustomerService;
import com.watch.shopwatchonline.Service.UserService;

@Controller
@RequestMapping("api/admin/user")
public class UserrController {
    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
//
//    @GetMapping("/add-customer")
//    public String add(Model model) {
//        CustomerDto dto = new CustomerDto();
//        dto.setIsEdit(false);
//        model.addAttribute("customer", dto);
//        return "web-admin/Addcustomer";
//    }
//
//    @GetMapping("/edit/{customerId}")
//    public ModelAndView edit(ModelMap model, @PathVariable("customerId") Integer customerId) {
//
//        Optional<Customer> opt = customerService.findById(customerId);
//        CustomerDto dto = new CustomerDto();
//
//        if (opt.isPresent()) {
//            Customer entity = opt.get();
//
//            BeanUtils.copyProperties(entity, dto);
//            dto.setIsEdit(true);
//
//            model.addAttribute("customer", dto);
//
//            return new ModelAndView("web-admin/Addcustomer", model);
//        }
//
//        model.addAttribute("message", "Customer is existed");
//
//        return new ModelAndView("forward:/api/admin/customers", model);
//    }

	   @GetMapping("/delete")
	    public @ResponseBody ModelAndView delete(@RequestParam(name = "id") String id) {
	        userService.deleteById(Integer.parseInt(id));
	        return new ModelAndView("forward:/api/admin/user");
	    }

//    @PostMapping("/saveOrUpdate")
//    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("customer") CustomerDto dto,
//            BindingResult result) {
//
//        if(result.hasErrors()) {
//            return new ModelAndView("web-admin/Addcustomer");
//        }
//        Customer entity = new Customer();
//        BeanUtils.copyProperties(dto, entity);
//
//        entity.setStatus((short) 1);
//        customerService.save(entity);
//
//        model.addAttribute("message", "Customer is saved!");
//
//        return new ModelAndView("redirect:/api/admin/customers", model);
//    }

    @RequestMapping("")
    public String list(ModelMap model) {
        List<User> list = userService.findAll();

        model.addAttribute("users", list);
        return "web-admin/listcustomer";
    }
//
//    @GetMapping("search")
//    public String search() {
//        return "web-admin/customer";
//    }
}
