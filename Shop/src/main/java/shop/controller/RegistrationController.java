package shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import shop.domain.User;
import shop.service.UserService;

@Controller
@RequestMapping("/signUp")
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public User user() {
		return new User();
	}

	@GetMapping()
	public String showSignUpForm() {
		return "signUp";
	}
	
	@PostMapping
	public String signUp(Model model, User user) {
		if(!userService.checkExistUsername(user.getUsername())) {
			model.addAttribute("errMes", "The username is already registered");
			return "signUp";
		}
		userService.addClient(user);
		return "redirect:/login";
	}
}
