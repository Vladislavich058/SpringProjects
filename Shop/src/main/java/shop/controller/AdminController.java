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

import shop.domain.Smartphone;
import shop.domain.User;
import shop.service.ClientOrderService;
import shop.service.ClientService;
import shop.service.RateService;
import shop.service.SmartphoneService;
import shop.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ClientService clientService;
	
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
		return "admin";
	}
	
	@PostMapping
	public String editAccount(Model model, User editUser, @AuthenticationPrincipal User authUser) {
		if(!userService.checkExistUsernameEdit(editUser, authUser)) {
			model.addAttribute("errMes", "The username is already registered");
			return "admin";
		}
		userService.editUser(editUser, authUser);
		return "redirect:/admin";
	}
	
	@GetMapping("/clients")
	public String getClients(Model model) {
		model.addAttribute("clients", clientService.getClients());
		return "clientsList";
	}
	
	@GetMapping("/deleteClient")
	public String deleteClient(Long clientId) {
		userService.deleteUser(clientId);
		return "redirect:/admin/clients";
	}
	
	@GetMapping("/changeStatus")
	public String changeStatus(Long clientId) {
		clientService.changeStatus(clientId);
		return "redirect:/admin/clients";
	}
	
	@GetMapping("/findClients")
	public String findClients(Model model, String find) {
		model.addAttribute("clients", clientService.findClients(find));
		return "clientsList";
	}
	
	@GetMapping("/smartphones")
	public String getSmartphones(Model model) {
		model.addAttribute("smartphones", smartphoneService.getSmartphones());
		return "smartphonesList";
	}
	
	@GetMapping("/deleteSmartphone")
	public String deleteSmartphone(Long smartphoneId) {
		smartphoneService.deleteSmartphone(smartphoneId);
		return "redirect:/admin/smartphones";
	}
	
	@ModelAttribute
	public Smartphone smartphone() {
		return new Smartphone();
	}
	
	@GetMapping("/addSmartphone")
	public String addSmartphone() {
		return "smartphoneForm";
	}
	
	@PostMapping("/addSmartphone")
	public String addSmartphone(Model model, Smartphone smartphone) {
		if(!smartphoneService.checkExistName(smartphone.getName(), smartphone.getInternalMemory())) {
			model.addAttribute("errMes", "The smartphone already exists!");
			return "smartphoneForm";
		}
		smartphoneService.addSmartphone(smartphone);
		return "redirect:/admin/smartphones";
	}
	
	@GetMapping("/editSmartphone")
	public String editSmartphone(Model model, Long smartphoneId) {
		model.addAttribute("edit","edit");
		model.addAttribute("smartphone", smartphoneService.getSmartphone(smartphoneId));
		return "smartphoneForm";
	}
	
	@PostMapping("/editSmartphone")
	public String editSmartphone(Model model, Smartphone smartphone) {
		if(!smartphoneService.checkExistNameEdit(smartphone)) {
			model.addAttribute("edit","edit");
			model.addAttribute("errMes", "The smartphone already exists!");
			return "smartphoneForm";
		}
		smartphoneService.editSmartphone(smartphone);
		return "redirect:/admin/smartphones";
	}
	
	@GetMapping("/findSmartphones")
	public String findSmartphones(Model model, String find) {
		model.addAttribute("smartphones", smartphoneService.findSmartphones(find));
		return "smartphonesList";
	}
	
	@GetMapping("/filterSmartphones")
	public String filterSmartphones(Model model, String fromPrice, String toPrice) {
		if(!smartphoneService.checkPriceInterval(fromPrice, toPrice)) {
			model.addAttribute("errMes","Please, check price interval!");
			return "smartphonesList";
		}
		model.addAttribute("smartphones", smartphoneService.filterSmartphones(fromPrice, toPrice));
		return "smartphonesList";
	}
	
	@GetMapping("/sortSmartphones")
	public String sortSmartphones(Model model, String sort) {
		model.addAttribute("smartphones", smartphoneService.sortSmartphones(sort));
		return "smartphonesList";
	}
	
	@GetMapping("/orders")
	public String getOrders(Model model) {
		model.addAttribute("orders", clientOrderService.getClientOrders());
		return "ordersList";
	}
	
	@GetMapping("/findOrders")
	public String findOrders(Model model, String find) {
		model.addAttribute("orders", clientOrderService.findClientOrders(find));
		return "ordersList";
	}
	
	@GetMapping("/filterOrders")
	public String filterOrders(Model model, LocalDate fromDate, LocalDate toDate) {
		if(!clientOrderService.checkDateInterval(fromDate, toDate)) {
			model.addAttribute("errMes","Please, check date interval!");
			return "ordersList";
		}
		model.addAttribute("orders", clientOrderService.filterClientOrders(fromDate, toDate));
		return "ordersList";
	}
	
	@GetMapping("/sortOrders")
	public String sortOrders(Model model, String sort) {
		model.addAttribute("orders", clientOrderService.sortClientOrders(sort));
		return "ordersList";
	}
	
	@GetMapping("/cancel")
	public String setCancel(Long clientOrderId) {
		clientOrderService.cancelOrder(clientOrderId);
		return "redirect:/admin/orders";
	}
	
	@GetMapping("/viewOrder")
	public String viewOrder(Model model, Long clientOrderId) {
		model.addAttribute("order", clientOrderService.getClientOrder(clientOrderId));
		return "orderView";
	}
	
	@GetMapping("/rating")
	public String rating(Model model) {
		model.addAttribute("smartphones", rateService.getRating());
		return "rating";
	}
	
	@GetMapping("/getStatistics")
	public String getStatistics(Model model, LocalDate date) {
		model.addAttribute("res", "res");
		model.addAttribute("smartphones", clientOrderService.getMostSoldedSmartphones(date));
		model.addAttribute("profit", clientOrderService.getProfits(date));
		model.addAttribute("canceled", clientOrderService.getCanceledPercent(date));
		model.addAttribute("delivered", clientOrderService.getDeliveredPercent(date));
		return "profits";
	}
	
}
