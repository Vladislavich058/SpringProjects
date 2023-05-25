package restaurant.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import restaurant.dao.DishRepository;
import restaurant.dao.DrinkRepository;
import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Dish;
import restaurant.domain.Dish.Type;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;
import restaurant.service.OrderServiceImpl;

@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

	@Autowired
	private OrderServiceImpl service;

	@Autowired
	private DrinkRepository drinkRepo;

	@Autowired
	private DishRepository dishRepo;

	@ModelAttribute
	public void addDishandDrinksToModel(Model model) {
		List<Dish> dishes = (List<Dish>) dishRepo.findAll();
		Type[] types = Dish.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(dishes, type));
		}
		List<Drink> drinks = (List<Drink>) drinkRepo.findAll();
		model.addAttribute("drinks", drinks);
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

	@ModelAttribute(name = "drink")
	public Drink drink() {
		return new Drink();
	}

	@ModelAttribute(name = "dish")
	public Dish dish() {
		return new Dish();
	}

	@ModelAttribute(name = "order")
	public DeliveryOrder deliveryOrder() {
		return new DeliveryOrder();
	}

	@GetMapping
	public String showMenu() {
		return "menu";
	}

	@GetMapping("/addDish")
	public String addDishInOrder(@ModelAttribute("order") DeliveryOrder order, Model model, @RequestParam Long id,
			@RequestParam Integer count) {
		service.addNewDishInOrder(order, id, count);
		return "redirect:/order";
	}

	@GetMapping("/deleteDish")
	public String deleteDishFromOrder(@ModelAttribute("order") DeliveryOrder order, Model model,
			@RequestParam Long id) {
		service.deleteDishFromOrder(order, id);
		return "redirect:/order";
	}

	@GetMapping("/addDrink")
	public String addDrinkInOrder(@ModelAttribute("order") DeliveryOrder order, Model model, @RequestParam Long id,
			@RequestParam Integer count) {
		service.addNewDrinkInOrder(order, id, count);
		return "redirect:/order";
	}

	@GetMapping("/deleteDrink")
	public String deleteDrinkFromOrder(@ModelAttribute("order") DeliveryOrder order, Model model,
			@RequestParam Long id) {
		service.deleteDrinkFromOrder(order, id);
		return "redirect:/order";
	}

	@PostMapping("/confirm")
	public String confirm(@Valid @ModelAttribute("order") DeliveryOrder order, Errors error, Model model,
			SessionStatus session, @AuthenticationPrincipal Client client) {

		if (error.hasErrors()) {
			return "menu";
		}

		if (!service.checkDeliveryDate(order)) {
			model.addAttribute("errorMessage", "You cannot place an order in less than an hour");
			return "menu";
		}

		service.confirmOrder(order, client);
		session.setComplete();
		return "redirect:/order";
	}

	private Iterable<Dish> filterByType(List<Dish> dishes, Type type) {
		return dishes.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}

}
