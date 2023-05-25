package shop.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import shop.component.ThymleafComponent;
import shop.domain.ClientOrder;
import shop.domain.User;
import shop.service.ClientOrderService;
import shop.service.RateService;
import shop.service.SmartphoneService;
import shop.service.UserService;

@Controller
@RequestMapping("/client")
@SessionAttributes("clientOrder")
public class ClientController {

	@Autowired
	private UserService userService;

	@Autowired
	private SmartphoneService smartphoneService;

	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private RateService rateService;

	@ModelAttribute
	public User user() {
		return new User();
	}

	@GetMapping
	public String showAccount(Model model, @AuthenticationPrincipal User authUser) {
		model.addAttribute("user", authUser);
		return "client";
	}

	@PostMapping
	public String editAccount(Model model, User editUser, @AuthenticationPrincipal User authUser) {
		if (!userService.checkExistUsernameEdit(editUser, authUser)) {
			model.addAttribute("errMes", "The username is already registered");
			return "client";
		}
		userService.editUser(editUser, authUser);
		return "redirect:/client";
	}

	@GetMapping("/deleteAccount")
	public String deleteAccount(@AuthenticationPrincipal User user, HttpServletRequest req) throws ServletException {
		req.logout();
		userService.deleteUser(user.getId());
		return "redirect:/login";
	}

	@GetMapping("/smartphones")
	public String getSmartphones(Model model) {
		model.addAttribute("client", "client");
		model.addAttribute("smartphones", smartphoneService.getSmartphones());
		return "smartphonesList";
	}

	@GetMapping("/findSmartphones")
	public String findSmartphones(Model model, String find) {
		model.addAttribute("client", "client");
		model.addAttribute("smartphones", smartphoneService.findSmartphones(find));
		return "smartphonesList";
	}

	@GetMapping("/filterSmartphones")
	public String filterSmartphones(Model model, String fromPrice, String toPrice) {
		if (!smartphoneService.checkPriceInterval(fromPrice, toPrice)) {
			model.addAttribute("client", "client");
			model.addAttribute("errMes", "Please, check price interval!");
			return "smartphonesList";
		}
		model.addAttribute("client", "client");
		model.addAttribute("smartphones", smartphoneService.filterSmartphones(fromPrice, toPrice));
		return "smartphonesList";
	}

	@GetMapping("/sortSmartphones")
	public String sortSmartphones(Model model, String sort) {
		model.addAttribute("client", "client");
		model.addAttribute("smartphones", smartphoneService.sortSmartphones(sort));
		return "smartphonesList";
	}

	@ModelAttribute(name = "clientOrder")
	public ClientOrder clientOrder() {
		return new ClientOrder();
	}

	@GetMapping("/basket")
	public String basket() {
		return "basket";
	}

	@GetMapping("/order/addSmartphone")
	public String addSmartphoneInOrder(@ModelAttribute("clientOrder") ClientOrder order, Model model, Long id,
			Integer count) {
		clientOrderService.addSmartphoneInOrder(order, count, id);
		return "redirect:/client/basket";
	}

	@GetMapping("/order/deleteSmartphone")
	public String deleteSmartphoneFromOrder(@ModelAttribute("clientOrder") ClientOrder order, Model model, Long smartphoneId) {
		clientOrderService.deleteSmartphoneFromOrder(order, smartphoneId);
		return "redirect:/client/basket";
	}

	@PostMapping("/order/confirm")
	public String confirm(@ModelAttribute("clientOrder") ClientOrder order, Model model, SessionStatus session,
			@AuthenticationPrincipal User user) {

		if (!clientOrderService.checkDate(order.getDate())) {
			model.addAttribute("errMes", "You cannot place an order in less than one day");
			return "basket";
		}

		clientOrderService.confirmOrder(order, user);
		session.setComplete();
		return "redirect:/client/orders";
	}
	
	@GetMapping("/orders")
	public String getOrders(Model model, @AuthenticationPrincipal User user) {
		model.addAttribute("orders", clientOrderService.getClientOrdersForClient(user));
		model.addAttribute("client","client");
		return "ordersList";
	}
	
	@GetMapping("/findOrders")
	public String findOrders(Model model, String find, @AuthenticationPrincipal User user) {
		model.addAttribute("orders", clientOrderService.findClientOrdersForClient(find, user));
		model.addAttribute("client","client");
		return "ordersList";
	}
	
	@GetMapping("/filterOrders")
	public String filterOrders(Model model, LocalDate fromDate, LocalDate toDate, @AuthenticationPrincipal User user) {
		if(!clientOrderService.checkDateInterval(fromDate, toDate)) {
			model.addAttribute("errMes","Please, check date interval!");
			model.addAttribute("client","client");
			return "ordersList";
		}
		model.addAttribute("client","client");
		model.addAttribute("orders", clientOrderService.filterClientOrdersForClient(fromDate, toDate, user));
		return "ordersList";
	}
	
	@GetMapping("/sortOrders")
	public String sortOrders(Model model, String sort, @AuthenticationPrincipal User user) {
		model.addAttribute("orders", clientOrderService.sortClientOrdersForClient(sort, user));
		model.addAttribute("client","client");
		return "ordersList";
	}
	
	@GetMapping("/cancel")
	public String setCancel(Long clientOrderId) {
		clientOrderService.cancelOrder(clientOrderId);
		return "redirect:/client/orders";
	}
	
	@GetMapping("/delivered")
	public String setSelivered(Long clientOrderId) {
		clientOrderService.confirmDelivery(clientOrderId);
		return "redirect:/client/viewOrder?clientOrderId="+clientOrderId;
	}
	
	@GetMapping("/viewOrder")
	public String viewOrder(Model model, Long clientOrderId, @AuthenticationPrincipal User user) {
		model.addAttribute("client", user);
		model.addAttribute("checker", ThymleafComponent.getInstance());
		model.addAttribute("order", clientOrderService.getClientOrder(clientOrderId));
		return "orderView";
	}
	
	@GetMapping("/rate")
	public String rate(Long smartphoneId, Integer stars, Long clientOrderId, @AuthenticationPrincipal User user) {
		rateService.addRate(smartphoneId, stars, user);
		return "redirect:/client/viewOrder?clientOrderId="+clientOrderId;
	}
}
