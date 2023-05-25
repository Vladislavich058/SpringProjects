package restaurant.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

	@GetMapping
	public String home() {
		return "delivery";
	}

	@ModelAttribute()
	public void userData(Model model, @AuthenticationPrincipal Admin admin, @AuthenticationPrincipal Client client,
			@AuthenticationPrincipal Manager manager, @AuthenticationPrincipal Messenger messenger) {
		if (admin != null)
			model.addAttribute("user", admin);
		if (client != null)
			model.addAttribute("user", client);
		if (manager != null)
			model.addAttribute("user", manager);
		if (messenger != null)
			model.addAttribute("user", messenger);
	}
}
