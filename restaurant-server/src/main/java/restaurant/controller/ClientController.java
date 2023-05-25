package restaurant.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.security.ClientRegistrationForm;
import restaurant.service.AdminService;
import restaurant.service.ClientService;
import restaurant.web.DateFormatter;

@Controller
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientService service;
	
	@Autowired
	private AdminService adminService;

	@ModelAttribute(name = "clientRegistrationForm")
	public ClientRegistrationForm adminRegistrationForm(@AuthenticationPrincipal Client client) {
		return service.showClientRedactForm(client);
	}

	@GetMapping("/account")
	public String showAccount(Model model, @AuthenticationPrincipal Client client) {
		model.addAttribute("client", client);
		return "clientAccount";
	}

	@GetMapping("/deleteTrue")
	public String deleteAccount(Model model, HttpServletRequest req, @AuthenticationPrincipal Client client)
			throws ServletException {
		service.deleteAccount(client);
		req.logout();
		return "redirect:/";
	}

	@GetMapping("/delete")
	public String deleteAccount(Model model) {
		model.addAttribute("deleteFlag", "deleteFlag");
		return "clientAccount";
	}

	@GetMapping("/redact")
	public String showRedact(Model model, @AuthenticationPrincipal Client client) {
		model.addAttribute("redactFlag", "redactFlag");
		return "clientAccount";
	}

	@PostMapping("/account")
	public String redactManager(Model model, @Valid ClientRegistrationForm form, Errors error,
			@AuthenticationPrincipal Client client) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "clientAccount";
		}
		
		if (!adminService.checkExistClient(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "clientAccount";
		}

		service.redactAccount(client, form);

		return "redirect:/client/account";
	}

	@GetMapping("/orders")
	public String showOrders(Model model, @AuthenticationPrincipal Client client) {
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		model.addAttribute("orders", service.showClientOrders(client));
		return "ordersList";
	}

	@GetMapping("/order/cancel")
	public String deleteOrder(@RequestParam Long id, @AuthenticationPrincipal Client client) {
		service.cancelClientOrder(id, client);
		return "redirect:/client/orders";
	}
	
	@GetMapping("/order/print")
	public String deleteOrder(@RequestParam Long id) {
		service.print(id);
		return "redirect:/client/orders";
	}

	@GetMapping("/orders/find")
	public String findOrder(Model model, @RequestParam Long id, @AuthenticationPrincipal Client client) {
		model.addAttribute("orders", service.findClientOrder(id, client));
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/orders/filter")
	public String filterOrder(Model model, @RequestParam String dateFrom, @RequestParam String dateTo,
			@AuthenticationPrincipal Client client) {
		model.addAttribute("dateFormatter", DateFormatter.getInstance());

		if (!service.checkDateInterval(dateFrom, dateTo)) {
			model.addAttribute("errorMessage", "Date from can't be more than date to");
			model.addAttribute("orders", new ArrayList<DeliveryOrder>());
			return "ordersList";
		}

		model.addAttribute("orders", service.filterClientOrders(dateFrom, dateTo, client));
		return "ordersList";
	}

	@GetMapping("/orders/sort")
	public String sortOrder(Model model, @RequestParam String choice, @AuthenticationPrincipal Client client) {
		model.addAttribute("orders", service.sortClientOrders(choice, client));
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/order/view")
	public String showOrderView(Model model, @RequestParam Long id) {
		model.addAttribute("order", service.showClientOrderView(id));
		model.addAttribute("viewFlag", "viewFlag");
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}
}
