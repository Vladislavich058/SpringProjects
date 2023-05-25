package restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;
import restaurant.service.RegistrationService;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private RegistrationService service;

	@ModelAttribute(name = "messengerRegistrationForm")
	public MessengerRegistrationForm messengerRegistrationForm() {
		return new MessengerRegistrationForm();
	}

	@ModelAttribute(name = "managerRegistrationForm")
	public ManagerRegistrationForm managerRegistrationForm() {
		return new ManagerRegistrationForm();
	}

	@ModelAttribute(name = "clientRegistrationForm")
	public ClientRegistrationForm clientRegistrationForm() {
		return new ClientRegistrationForm();
	}

	@GetMapping("/client")
	public String registerClientForm() {
		return "registrationClient";
	}

	@GetMapping("/manager")
	public String registerManagerForm() {
		return "registrationManager";
	}

	@GetMapping("/messenger")
	public String registerMessengerForm() {
		return "registrationMessenger";
	}

	@PostMapping("/client")
	public String processClientRegistration(Model model, @Valid ClientRegistrationForm form, Errors error) {
		if (error.hasErrors())
			return "registrationClient";

		if (!service.checkExistUser(form.getUsername())) {
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "registrationClient";
		}

		service.saveNewClient(form);
		return "redirect:/login";
	}

	@PostMapping("/manager")
	public String processManagerRegistration(Model model, @Valid ManagerRegistrationForm form, Errors error) {
		if (error.hasErrors())
			return "registrationManager";

		if (!service.checkExistUser(form.getUsername())) {
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "registrationManager";
		}

		service.saveNewManager(form);
		return "redirect:/login";
	}

	@PostMapping("/messenger")
	public String processMessengerRegistration(Model model, @Valid MessengerRegistrationForm form, Errors error) {

		if (error.hasErrors())
			return "registrationMessenger";

		if (!service.checkExistUser(form.getUsername())) {
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "registrationMessenger";
		}

		service.saveNewMessenger(form);
		return "redirect:/login";
	}
}
