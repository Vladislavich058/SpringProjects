package restaurant.controller;

import java.time.LocalDateTime;
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
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Messenger;
import restaurant.security.MessengerRegistrationForm;
import restaurant.service.AdminServiceImpl;
import restaurant.service.MessengerServiceImpl;
import restaurant.web.DateFormatter;

@Controller
@RequestMapping("/messenger")
public class MessengerController {

	@Autowired
	private MessengerServiceImpl service;
	
	@Autowired
	private AdminServiceImpl adminService;

	@ModelAttribute(name = "messengerRegistrationForm")
	public MessengerRegistrationForm messengerRegistrationForm(@AuthenticationPrincipal Messenger messenger) {
		return service.showMessengerRedactForm(messenger);
	}

	@GetMapping("/account")
	public String showAccount(Model model, @AuthenticationPrincipal Messenger messenger) {
		model.addAttribute("messenger", messenger);
		return "messengerAccount";
	}

	@GetMapping("/deleteTrue")
	public String deleteAccount(Model model, HttpServletRequest req, @AuthenticationPrincipal Messenger messenger)
			throws ServletException {
		service.deleteAccount(messenger);
		req.logout();
		return "redirect:/";
	}

	@GetMapping("/delete")
	public String deleteAccount(Model model) {
		model.addAttribute("deleteFlag", "deleteFlag");
		return "messengerAccount";
	}

	@GetMapping("/redact")
	public String showRedact(Model model, @AuthenticationPrincipal Messenger messenger) {
		model.addAttribute("redactFlag", "redactFlag");
		return "messengerAccount";
	}

	@PostMapping("/account")
	public String redactManager(Model model, @Valid MessengerRegistrationForm form, Errors error,
			@AuthenticationPrincipal Messenger messenger) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "messengerAccount";
		}
		
		if (!adminService.checkExistMessenger(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "messengerAccount";
		}

		service.redactAccount(messenger, form);

		return "redirect:/messenger/account";
	}

	@GetMapping("/orders")
	public String showOrders(Model model) {
		model.addAttribute("orders", service.showOrders());
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/order/view")
	public String showOrderView(Model model, @RequestParam Long id) {
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("order", service.showOrderView(id));
		model.addAttribute("viewFlag", "viewFlag");
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/orders/find")
	public String findOrder(Model model, @RequestParam Long id) {
		model.addAttribute("orders", service.findOrder(id));
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/orders/filter")
	public String filterOrder(Model model, @RequestParam String dateFrom, @RequestParam String dateTo) {
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		if (!service.checkDateInterval(dateFrom, dateTo)) {
			model.addAttribute("errorMessage", "Date from can't be more than date to");
			model.addAttribute("orders", new ArrayList<DeliveryOrder>());
			model.addAttribute("messengerFlag", "messengerFlag");
			return "ordersList";
		}
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("orders", service.filterOrders(dateFrom, dateTo));
		return "ordersList";
	}

	@GetMapping("/orders/sort")
	public String sortOrder(Model model, @RequestParam String choice) {
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("orders", service.sortOrders(choice));
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/order/deliver")
	public String confirmOrder(@RequestParam Long id, @AuthenticationPrincipal Messenger messenger) {
		service.confirmOrder(id, messenger);
		return "redirect:/messenger/orders";
	}

	@GetMapping("/deliveries")
	public String showDeliveries(Model model, @AuthenticationPrincipal Messenger messenger) {
		model.addAttribute("orders", service.showDeliveries(messenger));
		model.addAttribute("messengerFlag", "messengerFlag");
		return "deliveriesList";
	}

	@GetMapping("/delivery/cancel")
	public String cancelDelivery(@RequestParam Long id) {
		service.cancelDelivery(id);
		return "redirect:/messenger/deliveries";
	}

	@GetMapping("/delivery/view")
	public String showDeliveryView(Model model, @RequestParam Long id) {
		DeliveryOrder order = service.showOrderView(id);

		if (order.getStatus().equals("completed")) {
			if (order.getDeliveredAt().isAfter(LocalDateTime.of(order.getDeliveryDate(), order.getDeliveryTime()))) {
				model.addAttribute("completed", "Expired");
			} else {
				model.addAttribute("completed", "In time");
			}
		}

		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("order", order);
		model.addAttribute("viewFlag", "viewFlag");
		return "deliveriesList";
	}

	@GetMapping("/delivery/confirm")
	public String confirmDelivery(@RequestParam Long id) {
		service.confirmDelivery(id);
		return "redirect:/messenger/deliveries";
	}

	@GetMapping("/deliveries/find")
	public String findDelivery(Model model, @RequestParam Long id, @AuthenticationPrincipal Messenger messenger) {
		model.addAttribute("orders", service.findDelivery(id, messenger));
		model.addAttribute("messengerFlag", "messengerFlag");
		return "deliveriesList";
	}

	@GetMapping("/deliveries/filter")
	public String filterDelivery(Model model, @RequestParam String dateFrom, @RequestParam String dateTo,
			@AuthenticationPrincipal Messenger messenger) {

		if (!service.checkDateInterval(dateFrom, dateTo)) {
			model.addAttribute("errorMessage", "Date from can't be more than date to");
			model.addAttribute("orders", new ArrayList<DeliveryOrder>());
			model.addAttribute("messengerFlag", "messengerFlag");
			return "deliveriesList";
		}
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("orders", service.filterDeliveries(dateFrom, dateTo, messenger));
		return "deliveriesList";
	}

	@GetMapping("/deliveries/sort")
	public String sortDeliveries(Model model, @RequestParam String choice,
			@AuthenticationPrincipal Messenger messenger) {
		model.addAttribute("messengerFlag", "messengerFlag");
		model.addAttribute("orders", service.sortDeliveries(choice, messenger));

		return "deliveriesList";
	}
}
