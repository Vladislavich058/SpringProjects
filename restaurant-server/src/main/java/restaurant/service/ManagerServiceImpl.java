package restaurant.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import restaurant.dao.DeliveryOrderRepository;
import restaurant.dao.DishRepository;
import restaurant.dao.DrinkRepository;
import restaurant.dao.ManagerRepository;
import restaurant.dao.OrderDishCounterRepository;
import restaurant.dao.OrderDrinkCounterRepository;
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Dish;
import restaurant.domain.Dish.Type;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.pdfConverter.PdfFileExporter;
import restaurant.security.ManagerRegistrationForm;
import restaurant.web.DateFormatter;
import restaurant.web.ThymeMath;

@Service
public class ManagerServiceImpl implements ManagerService {

	private ManagerRepository managerRepo;
	private DishRepository dishRepo;
	private DrinkRepository drinkRepo;
	private DeliveryOrderRepository deliveryOrderRepo;
	private OrderDishCounterRepository orderDishCounterRepository;
	private OrderDrinkCounterRepository orderDrinkCounterRepository;

	private PasswordEncoder passwordEncoder;

	public ManagerServiceImpl(PasswordEncoder passwordEncoder, DishRepository dishRepo, DrinkRepository drinkRepo,
			ManagerRepository managerRepo, DeliveryOrderRepository deliveryOrderRepo,
			OrderDrinkCounterRepository orderDrinkCounterRepository,
			OrderDishCounterRepository orderDishCounterRepository) {
		this.managerRepo = managerRepo;
		this.dishRepo = dishRepo;
		this.drinkRepo = drinkRepo;
		this.passwordEncoder = passwordEncoder;
		this.deliveryOrderRepo = deliveryOrderRepo;
		this.orderDishCounterRepository = orderDishCounterRepository;
		this.orderDrinkCounterRepository = orderDrinkCounterRepository;
	}

	@Override
	public ManagerRegistrationForm showManagerRedactForm(Manager manager) {
		return new ManagerRegistrationForm(manager.getId(),manager.getStaffInfo().getUser().getUsername(), "",
				manager.getStaffInfo().getUser().getName(), manager.getStaffInfo().getUser().getSurname(),
				manager.getStaffInfo().getUser().getLastname(), manager.getStaffInfo().getUser().getPhone(),
				manager.getStaffInfo().getWorkExperience(), manager.getStaffInfo().getPassportNumber(),
				manager.getWorkPhone());
	}

	@Override
	public Dish showDishRedact(Long id) {
		dishRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
		return dishRepo.findById(id).get();
	}

	@Override
	public Drink showDrinkRedact(Long id) {
		drinkRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid drink Id:" + id));
		return drinkRepo.findById(id).get();
	}

	@Override
	public void deleteAccount(Manager manager) {
		managerRepo.deleteById(manager.getId());
	}

	@Override
	public void redactAccount(Manager manager, ManagerRegistrationForm form) {
		manager.getStaffInfo().getUser().setUsername(form.getUsername());
		manager.getStaffInfo().getUser().setPassword(passwordEncoder.encode(form.getPassword()));
		manager.getStaffInfo().getUser().setName(form.getName());
		manager.getStaffInfo().getUser().setSurname(form.getSurname());
		manager.getStaffInfo().getUser().setLastname(form.getLastname());
		manager.getStaffInfo().getUser().setPhone(form.getPhone());

		manager.getStaffInfo().setPassportNumber(form.getPassportNumber());
		manager.getStaffInfo().setWorkExperience(form.getWorkExperience());

		manager.setWorkPhone(form.getWorkPhone());

		managerRepo.save(manager);
	}

	@Override
	public List<Dish> showDishes() {
		return (List<Dish>) dishRepo.findAll();
	}

	@Override
	public List<Drink> showDrinks() {
		return (List<Drink>) drinkRepo.findAll();
	}

	@Override
	public void deleteDish(Long id) {
		dishRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish Id:" + id));
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();
		for (int i = 0; i < orders.size(); i++) {
			for (int j = 0; j < orderDishCounterRepository.findByDishId(id).size(); j++) {
				if (orders.get(i).getOrderDishCounteres()
						.contains(orderDishCounterRepository.findByDishId(id).get(j))) {
					orders.get(i).deleteDish(orderDishCounterRepository.findByDishId(id).get(j));
					deliveryOrderRepo.save(orders.get(i));
				}
			}
		}
		orderDishCounterRepository.deleteByDishId(id);
		dishRepo.deleteById(id);
	}

	@Override
	public void deleteDrink(Long id) {
		drinkRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid drink Id:" + id));
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();
		for (int i = 0; i < orders.size(); i++) {
			for (int j = 0; j < orderDrinkCounterRepository.findByDrinkId(id).size(); j++) {
				if (orders.get(i).getOrderDrinkCounteres()
						.contains(orderDrinkCounterRepository.findByDrinkId(id).get(j))) {
					orders.get(i).deleteDrink(orderDrinkCounterRepository.findByDrinkId(id).get(j));
					deliveryOrderRepo.save(orders.get(i));
				}
			}
		}
		orderDrinkCounterRepository.deleteByDrinkId(id);
		drinkRepo.deleteById(id);
	}

	@Override
	public void addDish(Dish dish) {
		dishRepo.save(dish);
	}

	@Override
	public void addDrink(Drink drink) {
		drinkRepo.save(drink);
	}

	@Override
	public List<Dish> findDish(String name) {
		Optional<Dish> dish = dishRepo.findByName(name);

		List<Dish> dishes = new ArrayList<>();
		if (!dish.isEmpty())
			dishes.add(dish.get());

		return dishes;
	}

	@Override
	public List<Drink> findDrink(String name) {
		Optional<Drink> drink = drinkRepo.findByName(name);

		List<Drink> drinks = new ArrayList<>();
		if (!drink.isEmpty())
			drinks.add(drink.get());

		return drinks;
	}

	@Override
	public List<Dish> sortDish(String choice) {
		List<Dish> dishes = (List<Dish>) dishRepo.findAll();

		if (Integer.parseInt(choice) == 1) {
			Collections.sort(dishes, new Comparator<Dish>() {
				@Override
				public int compare(Dish d1, Dish d2) {
					return Float.compare(d1.getPrice(), d2.getPrice());
				}
			});
		}

		if (Integer.parseInt(choice) == 2) {
			Collections.sort(dishes, new Comparator<Dish>() {
				@Override
				public int compare(Dish d1, Dish d2) {
					return d1.getKcal() - d2.getKcal();
				}
			});
		}

		if (Integer.parseInt(choice) == 3) {
			Collections.sort(dishes, new Comparator<Dish>() {
				@Override
				public int compare(Dish d1, Dish d2) {
					return d1.getWeight() - d2.getWeight();
				}
			});
		}
		
		if (Integer.parseInt(choice) == 4) {
			dishes = dishRepo.findByType(Type.SOUP);
		}
		
		if (Integer.parseInt(choice) == 5) {
			dishes = dishRepo.findByType(Type.SALAD);
		}
		
		if (Integer.parseInt(choice) == 6) {
			dishes = dishRepo.findByType(Type.HOT_DISH);
		}
		
		if (Integer.parseInt(choice) == 7) {
			dishes = dishRepo.findByType(Type.COLD_DISH);
		}
	
		if (Integer.parseInt(choice) == 8) {
			dishes = dishRepo.findByType(Type.SNACKS);
		}
		
		if (Integer.parseInt(choice) == 9) {
			dishes = dishRepo.findByType(Type.DESSERT);
		}
		return dishes;
	}

	@Override
	public List<Drink> sortDrink() {
		List<Drink> drinks = (List<Drink>) drinkRepo.findAll();

		Collections.sort(drinks, new Comparator<Drink>() {
			@Override
			public int compare(Drink d1, Drink d2) {
				return Float.compare(d1.getPrice(), d2.getPrice());
			}
		});

		return drinks;
	}

	@Override
	public List<Dish> filterDish(String priceFrom, String priceTo) {
		return dishRepo.findDishesByPriceBetween(Float.parseFloat(priceFrom), Float.parseFloat(priceTo));
	}

	@Override
	public List<Drink> filterDrink(String priceFrom, String priceTo) {
		return drinkRepo.findDrinksByPriceBetween(Float.parseFloat(priceFrom), Float.parseFloat(priceTo));
	}

	@Override
	public boolean checkExistDishForAdd(Dish dish) {
		if (!dishRepo.findByName(dish.getName()).isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean checkExistDrinkForAdd(Drink drink) {
		if (!drinkRepo.findByName(drink.getName()).isEmpty())
			return false;
		return true;
	}

	@Override
	public boolean checkExistDishForRedact(Dish dish) {
		if (!dishRepo.findByName(dish.getName()).isEmpty()
				&& !dishRepo.findById(dish.getId()).get().getName().equals(dish.getName()))
			return false;
		return true;
	}

	@Override
	public boolean checkExistDrinkForRedact(Drink drink) {
		if (!drinkRepo.findByName(drink.getName()).isEmpty()
				&& !drinkRepo.findById(drink.getId()).get().getName().equals(drink.getName()))
			return false;
		return true;
	}

	@Override
	public boolean checkPriceInterval(String priceFrom, String priceTo) {
		if (Float.parseFloat(priceFrom) > Float.parseFloat(priceTo))
			return false;
		return true;
	}

	@Override
	public void redactDish(Dish redDish) {
		dishRepo.findById(redDish.getId()).ifPresent(dish -> {
			dish.setName(redDish.getName());
			dish.setKcal(redDish.getKcal());
			dish.setStructure(redDish.getStructure());
			dish.setType(redDish.getType());
			dish.setWeight(redDish.getWeight());
			dish.setPrice(redDish.getPrice());

			dishRepo.save(dish);
		});

	}

	@Override
	public void redactDrink(Drink redDrink) {
		drinkRepo.findById(redDrink.getId()).ifPresent(drink -> {
			drink.setName(redDrink.getName());
			drink.setPrice(redDrink.getPrice());

			drinkRepo.save(drink);
		});
	}

	@Override
	public DeliveryOrder showOrderView(Long id) {
		deliveryOrderRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		return deliveryOrderRepo.findById(id).get();
	}

	@Override
	public List<DeliveryOrder> showOrders() {
		return (List<DeliveryOrder>) deliveryOrderRepo.findAll();
	}

	@Override
	public List<DeliveryOrder> showDeliveries() {
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();
		return orders.stream().filter(o->o.getStatus().equals("completed") || o.getStatus().equals("on the way")).toList();
	}

	@Override
	public void deleteOrder(Long id) {
		deliveryOrderRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		deliveryOrderRepo.deleteById(id);
	}

	@Override
	public void cancelOrder(Long id) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setStatus("canceled");
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public void cancelDelivery(Long id) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setMessenger(null);
			order.setStatus("confirmed");
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public void confirmOrder(Long id) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setStatus("confirmed");
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public List<DeliveryOrder> findOrder(Long number) {
		Optional<DeliveryOrder> deliveryOrder = deliveryOrderRepo.findById(number);

		List<DeliveryOrder> orders = new ArrayList<>();
		if (!deliveryOrder.isEmpty())
			orders.add(deliveryOrder.get());

		return orders;
	}

	@Override
	public List<DeliveryOrder> findDelivery(Long number) {
		Optional<DeliveryOrder> deliveryOrder = deliveryOrderRepo.findById(number);

		List<DeliveryOrder> orders = new ArrayList<>();
		if (!deliveryOrder.isEmpty() && deliveryOrder.get().getMessenger() != null)
			orders.add(deliveryOrder.get());
		return orders;
	}

	@Override
	public List<DeliveryOrder> sortOrders(String choice) {
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();

		if (Integer.parseInt(choice) == 1) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					return Float.compare(o2.getDeliveryCost(), o2.getDeliveryCost());
				}
			});
		}

		if (Integer.parseInt(choice) == 2) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					if (o1.getDeliveryDate().isAfter(o2.getDeliveryDate()))
						return -1;
					if (o1.getDeliveryDate().isBefore(o2.getDeliveryDate()))
						return 1;
					if (o1.getDeliveryDate().equals(o2.getDeliveryDate()))
						return 0;
					return 1;
				}
			});
		}

		if (Integer.parseInt(choice) == 3) {
			orders = deliveryOrderRepo.findByStatus("in processing");
		}

		if (Integer.parseInt(choice) == 4) {
			orders = deliveryOrderRepo.findByStatus("confirmed");
		}

		if (Integer.parseInt(choice) == 5) {
			orders = deliveryOrderRepo.findByStatus("on the way");
		}

		if (Integer.parseInt(choice) == 6) {
			orders = deliveryOrderRepo.findByStatus("canceled");
		}

		return orders;
	}

	@Override
	public List<DeliveryOrder> sortDeliveries(String choice) {
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();

		if (Integer.parseInt(choice) == 1) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					return Float.compare(o2.getDeliveryCost(), o2.getDeliveryCost());
				}
			});
		}

		if (Integer.parseInt(choice) == 2) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					if (o1.getDeliveryDate().isAfter(o2.getDeliveryDate()))
						return -1;
					if (o1.getDeliveryDate().isBefore(o2.getDeliveryDate()))
						return 1;
					if (o1.getDeliveryDate().equals(o2.getDeliveryDate()))
						return 0;
					return 1;
				}
			});
		}

		if (Integer.parseInt(choice) == 3) {
			orders = deliveryOrderRepo.findByStatus("completed");
		}

		if (Integer.parseInt(choice) == 4) {
			orders = deliveryOrderRepo.findByStatus("on the way");
		}

		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getMessenger() == null) {
				orders.remove(i);
			}
		}
		return orders;
	}

	@Override
	public List<DeliveryOrder> filterOrders(String dateFrom, String dateTo) {
		return deliveryOrderRepo.findDeliveryOrderByDeliveryDateBetween(LocalDate.parse(dateFrom),
				LocalDate.parse(dateTo));
	}

	@Override
	public List<DeliveryOrder> filterDeliveries(String dateFrom, String dateTo) {
		List<DeliveryOrder> orders = new ArrayList<>();

		orders = deliveryOrderRepo.findDeliveryOrderByDeliveryDateBetween(LocalDate.parse(dateFrom),
				LocalDate.parse(dateTo));

		for (int i = 0; i < orders.size(); i++) {
			if (orders.get(i).getMessenger() == null) {
				orders.remove(i);
			}
		}
		return orders;
	}

	@Override
	public boolean checkDateInterval(String dateFrom, String dateTo) {
		if (LocalDate.parse(dateFrom).isAfter(LocalDate.parse(dateTo)))
			return false;
		return true;
	}
	
	@Override
	public void print(Long id) {
		PdfFileExporter pdfFileExporter = new PdfFileExporter();

		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("order", deliveryOrderRepo.findById(id).get());
		data.put("thymeMath", new ThymeMath());
		data.put("dateFormatter", DateFormatter.getInstance());
		pdfFileExporter.exportPdfFile("check", data, "output/order_"+id+".pdf");
	}

}
