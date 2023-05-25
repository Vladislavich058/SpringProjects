package restaurant.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import restaurant.dao.DishRepository;
import restaurant.dao.DrinkRepository;
import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.Dish;
import restaurant.domain.Dish.Type;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;

@Controller
@RequestMapping("/menu")
@SessionAttributes("order")
public class MenuController {

	private DishRepository dishRepo;
	private DrinkRepository drinkRepo;

	public MenuController(DishRepository dishRepo, DrinkRepository drinkRepo) {
		this.dishRepo = dishRepo;
		this.drinkRepo = drinkRepo;
	}

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

	@GetMapping
	public String showMenu() {
		return "menu";
	}

	private Iterable<Dish> filterByType(List<Dish> dishes, Type type) {
		return dishes.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}

}
