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
import restaurant.domain.Dish;
import restaurant.domain.Dish.Type;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.security.ManagerRegistrationForm;
import restaurant.service.AdminService;
import restaurant.service.ManagerService;
import restaurant.web.DateFormatter;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private AdminService adminService;

	public ManagerController(ManagerService managerService) {
		this.managerService = managerService;
	}

	@ModelAttribute(name = "managerRegistrationForm")
	public ManagerRegistrationForm managerRegistrationForm(@AuthenticationPrincipal Manager manager) {
		return managerService.showManagerRedactForm(manager);
	}

	@GetMapping("/account")
	public String showAccount(Model model, @AuthenticationPrincipal Manager manager) {
		model.addAttribute("manager", manager);
		return "managerAccount";
	}

	@GetMapping("/deleteTrue")
	public String deleteAccount(Model model, HttpServletRequest req, @AuthenticationPrincipal Manager manager)
			throws ServletException {
		managerService.deleteAccount(manager);
		req.logout();
		return "redirect:/";
	}

	@GetMapping("/delete")
	public String showDeleteWarning(Model model) {
		model.addAttribute("deleteFlag", "deleteFlag");
		return "managerAccount";
	}

	@GetMapping("/redact")
	public String showRedactAccount(Model model, @AuthenticationPrincipal Manager manager) {
		model.addAttribute("redactFlag", "redactFlag");
		return "managerAccount";
	}

	@PostMapping("/account")
	public String redactAccount(Model model, @Valid ManagerRegistrationForm form, Errors error,
			@AuthenticationPrincipal Manager manager) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "managerAccount";
		}
		
		if (!adminService.checkExistManager(form)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "User with this username already exists!");
			return "managerAccount";
		}

		managerService.redactAccount(manager, form);

		return "redirect:/manager/account";
	}

	@GetMapping("/dishes")
	public String showDishes(Model model) {
		model.addAttribute("dishes", managerService.showDishes());
		return "dishesList";
	}

	@GetMapping("/dish/delete")
	public String deleteDish(@RequestParam Long id) {
		managerService.deleteDish(id);
		return "redirect:/manager/dishes";
	}

	@GetMapping("/dishes/find")
	public String findDish(Model model, @RequestParam String name) {
		model.addAttribute("dishes", managerService.findDish(name));
		return "dishesList";
	}

	@GetMapping("/dishes/filter")
	public String filterDish(Model model, @RequestParam String priceFrom, @RequestParam String priceTo) {

		if (!managerService.checkPriceInterval(priceFrom, priceTo)) {
			model.addAttribute("errorMessage", "Price from can't be more than price to");
			model.addAttribute("dishes", new ArrayList<Dish>());
			return "dishesList";
		}

		model.addAttribute("dishes", managerService.filterDish(priceFrom, priceTo));
		return "dishesList";
	}

	@GetMapping("/dishes/sort")
	public String sortDish(Model model, @RequestParam String choice) {

		model.addAttribute("dishes", managerService.sortDish(choice));

		return "dishesList";
	}

	@GetMapping("/dish/redact")
	public String showDishRedact(Model model, @RequestParam Long id) {
		model.addAttribute("dish", managerService.showDishRedact(id));
		model.addAttribute("types", Type.values());
		model.addAttribute("redactFlag", "redactFlag");
		return "dishesList";
	}

	@PostMapping("/dish/redact")
	public String redactDish(Model model, @Valid Dish dish, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("types", Type.values());
			model.addAttribute("redactFlag", "redactFlag");
			return "dishesList";
		}

		if (!managerService.checkExistDishForRedact(dish)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "Dish with this name already exists!");
			return "dishesList";
		}

		managerService.redactDish(dish);

		return "redirect:/manager/dishes";
	}

	@GetMapping("/dish/add")
	public String showDishAdd(Model model) {
		model.addAttribute("types", Type.values());
		model.addAttribute("redactFlag", "redactFlag");
		model.addAttribute("addFlag", "addFlag");
		model.addAttribute("dish", new Dish());
		return "dishesList";
	}

	@PostMapping("/dish/add")
	public String addDish(Model model, @Valid Dish dish, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("types", Type.values());
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("addFlag", "addFlag");
			return "dishesList";
		}

		if (!managerService.checkExistDishForAdd(dish)) {
			model.addAttribute("types", Type.values());
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("addFlag", "addFlag");
			model.addAttribute("errorMessage", "Dish with this name already exists!");
			return "dishesList";
		}

		managerService.addDish(dish);

		return "redirect:/manager/dishes";
	}

	@GetMapping("/drinks")
	public String showDrinks(Model model) {
		model.addAttribute("drinks", managerService.showDrinks());
		return "drinksList";
	}

	@GetMapping("/drink/delete")
	public String deleteDrink(@RequestParam Long id) {
		managerService.deleteDrink(id);
		return "redirect:/manager/drinks";
	}

	@GetMapping("/drinks/find")
	public String findDrink(Model model, @RequestParam String name) {
		model.addAttribute("drinks", managerService.findDrink(name));
		return "drinksList";
	}

	@GetMapping("/drinks/filter")
	public String filterDrink(Model model, @RequestParam String priceFrom, @RequestParam String priceTo) {

		if (!managerService.checkPriceInterval(priceFrom, priceTo)) {
			model.addAttribute("errorMessage", "Price from can't be more than price to");
			model.addAttribute("drinks", new ArrayList<Drink>());
			return "dishesList";
		}

		model.addAttribute("drinks", managerService.filterDrink(priceFrom, priceTo));
		return "drinksList";
	}

	@GetMapping("/drinks/sort")
	public String sortDrink(Model model) {
		model.addAttribute("drinks", managerService.sortDrink());
		return "drinksList";
	}

	@GetMapping("/drink/redact")
	public String showDrinkRedact(Model model, @RequestParam Long id) {
		model.addAttribute("drink", managerService.showDrinkRedact(id));
		model.addAttribute("redactFlag", "redactFlag");
		return "drinksList";
	}

	@PostMapping("/drink/redact")
	public String redactDish(Model model, @Valid Drink drink, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			return "drinksList";
		}

		if (!managerService.checkExistDrinkForRedact(drink)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("errorMessage", "Drink with this name already exists!");
			return "drinksList";
		}

		managerService.redactDrink(drink);

		return "redirect:/manager/drinks";
	}

	@GetMapping("/drink/add")
	public String showDrinkAdd(Model model) {
		model.addAttribute("redactFlag", "redactFlag");
		model.addAttribute("addFlag", "addFlag");
		model.addAttribute("drink", new Drink());
		return "drinksList";
	}

	@PostMapping("/drink/add")
	public String addDrink(Model model, @Valid Drink drink, Errors error) {

		if (error.hasErrors()) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("addFlag", "addFlag");
			return "drinksList";
		}

		if (!managerService.checkExistDrinkForAdd(drink)) {
			model.addAttribute("redactFlag", "redactFlag");
			model.addAttribute("addFlag", "addFlag");
			model.addAttribute("errorMessage", "Dish with this name already exists!");
			return "drinksList";
		}

		managerService.addDrink(drink);

		return "redirect:/manager/drinks";
	}

	@GetMapping("/orders")
	public String showOrders(Model model) {
		model.addAttribute("orders", managerService.showOrders());
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		model.addAttribute("managerFlag", "managerFlag");
		return "ordersList";
	}

	@GetMapping("/order/delete")
	public String deleteOrder(@RequestParam Long id) {
		managerService.deleteOrder(id);
		return "redirect:/manager/orders";
	}

	@GetMapping("/order/cancel")
	public String cancelOrder(@RequestParam Long id) {
		managerService.cancelOrder(id);
		return "redirect:/manager/orders";
	}

	@GetMapping("/order/confirm")
	public String confirmOrder(@RequestParam Long id) {
		managerService.confirmOrder(id);
		return "redirect:/manager/orders";
	}

	@GetMapping("/orders/find")
	public String findOrder(Model model, @RequestParam Long id) {
		model.addAttribute("orders", managerService.findOrder(id));
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		model.addAttribute("managerFlag", "managerFlag");
		return "ordersList";
	}

	@GetMapping("/orders/filter")
	public String filterOrder(Model model, @RequestParam String dateFrom, @RequestParam String dateTo) {
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		if (!managerService.checkDateInterval(dateFrom, dateTo)) {
			model.addAttribute("errorMessage", "Date from can't be more than date to");
			model.addAttribute("managerFlag", "managerFlag");
			model.addAttribute("orders", new ArrayList<DeliveryOrder>());
			return "ordersList";
		}
		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("orders", managerService.filterOrders(dateFrom, dateTo));
		return "ordersList";
	}

	@GetMapping("/orders/sort")
	public String sortOrder(Model model, @RequestParam String choice) {
		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("orders", managerService.sortOrders(choice));
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}

	@GetMapping("/order/view")
	public String showOrderView(Model model, @RequestParam Long id) {
		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("order", managerService.showOrderView(id));
		model.addAttribute("viewFlag", "viewFlag");
		model.addAttribute("dateFormatter", DateFormatter.getInstance());
		return "ordersList";
	}
	
	@GetMapping("/order/print")
	public String printOrder(@RequestParam Long id) {
		managerService.print(id);
		return "redirect:/manager/orders";
	}

	@GetMapping("/deliveries")
	public String showDeliveries(Model model) {
		model.addAttribute("orders", managerService.showDeliveries());
		model.addAttribute("managerFlag", "managerFlag");
		return "deliveriesList";
	}

	@GetMapping("/delivery/cancel")
	public String cancelDelivery(@RequestParam Long id) {
		managerService.cancelDelivery(id);
		return "redirect:/manager/deliveries";
	}

	@GetMapping("/delivery/view")
	public String showDeliveryView(Model model, @RequestParam Long id) {

		if (managerService.showOrderView(id).getStatus().equals("completed")) {
			if (managerService.showOrderView(id).getDeliveredAt()
					.isAfter(LocalDateTime.of(managerService.showOrderView(id).getDeliveryDate(),
							managerService.showOrderView(id).getDeliveryTime()))) {
				model.addAttribute("completed", "Expired");
			} else {
				model.addAttribute("completed", "In time");
			}
		}

		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("order", managerService.showOrderView(id));
		model.addAttribute("viewFlag", "viewFlag");
		return "deliveriesList";
	}

	@GetMapping("/deliveries/find")
	public String findDelivery(Model model, @RequestParam Long id) {
		model.addAttribute("orders", managerService.findDelivery(id));
		model.addAttribute("managerFlag", "managerFlag");
		return "deliveriesList";
	}

	@GetMapping("/deliveries/filter")
	public String filterDelivery(Model model, @RequestParam String dateFrom, @RequestParam String dateTo) {
		if (!managerService.checkDateInterval(dateFrom, dateTo)) {
			model.addAttribute("errorMessage", "Date from can't be more than date to");
			model.addAttribute("orders", new ArrayList<DeliveryOrder>());
			model.addAttribute("messengerFlag", "messengerFlag");
			return "deliveriesList";
		}
		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("orders", managerService.filterDeliveries(dateFrom, dateTo));
		return "deliveriesList";
	}

	@GetMapping("/deliveries/sort")
	public String sortDeliveries(Model model, @RequestParam String choice) {
		model.addAttribute("managerFlag", "managerFlag");
		model.addAttribute("orders", managerService.sortDeliveries(choice));

		return "deliveriesList";
	}
}
