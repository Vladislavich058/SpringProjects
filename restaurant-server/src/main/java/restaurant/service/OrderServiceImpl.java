package restaurant.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import restaurant.dao.DeliveryOrderRepository;
import restaurant.dao.DishRepository;
import restaurant.dao.DrinkRepository;
import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Dish;
import restaurant.domain.Drink;
import restaurant.domain.OrderDishCounter;
import restaurant.domain.OrderDrinkCounter;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private DishRepository dishRepo;

	@Autowired
	private DrinkRepository drinkRepo;

	@Autowired
	private DeliveryOrderRepository deliveryOrderRepo;

	private DecimalFormat df = new DecimalFormat("#.##");

	@Override
	public void deleteDishFromOrder(DeliveryOrder order, Long id) {
		Dish dish = dishRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish id: " + id));

		for (int i = 0; i < order.getOrderDishCounteres().size(); i++) {
			if (order.getOrderDishCounteres().get(i).getDish().getId() == id) {
				Float price = order.getDeliveryCost();
				price -= order.getOrderDishCounteres().get(i).getCount() * dish.getPrice();
				order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
				order.getOrderDishCounteres().remove(order.getOrderDishCounteres().get(i));
			}
		}
	}

	@Override
	public void deleteDrinkFromOrder(DeliveryOrder order, Long id) {
		Drink drink = drinkRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid drink id: " + id));

		for (int i = 0; i < order.getOrderDrinkCounteres().size(); i++) {
			if (order.getOrderDrinkCounteres().get(i).getDrink().getId() == id) {
				Float price = order.getDeliveryCost();
				price -= order.getOrderDrinkCounteres().get(i).getCount() * drink.getPrice();
				order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
				order.getOrderDrinkCounteres().remove(order.getOrderDrinkCounteres().get(i));
			}
		}
	}

	@Transactional
	@Override
	public void confirmOrder(DeliveryOrder order, Client client) {
		order.setStatus("in processing");
		order.setDeliveryClient(client);
		order.setCreatedAt(LocalDateTime.now());
		deliveryOrderRepo.save(order);
	}

	@Override
	public boolean checkDeliveryDate(DeliveryOrder order) {
		if (order.getDeliveryDate().equals(LocalDate.now())
				&& order.getDeliveryTime().isBefore(LocalTime.now().plusHours(1)))
			return false;
		return true;
	}

	@Override
	public boolean addNewDishInOrder(DeliveryOrder order, Long id, Integer count) {
		Dish dish = dishRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid dish id: " + id));

		for (OrderDishCounter counter : order.getOrderDishCounteres()) {
			if (counter.getDish().getId() == id) {
				Integer countDish = counter.getCount();
				countDish += count;
				Float price = order.getDeliveryCost();
				price += count * dish.getPrice();
				order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
				counter.setCount(countDish);
				return true;
			}
		}

		OrderDishCounter counter = new OrderDishCounter(count, dish);

		Float price = order.getDeliveryCost();
		price += count * dish.getPrice();

		order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
		order.addDish(counter);
		return true;
	}

	@Override
	public boolean addNewDrinkInOrder(DeliveryOrder order, Long id, Integer count) {
		Drink drink = drinkRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid drink id: " + id));

		for (OrderDrinkCounter counter : order.getOrderDrinkCounteres()) {
			if (counter.getDrink().getId() == id) {
				Integer countDrink = counter.getCount();
				countDrink += count;
				Float price = order.getDeliveryCost();
				price += count * drink.getPrice();
				order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
				counter.setCount(countDrink);
				return true;
			}
		}

		OrderDrinkCounter counter = new OrderDrinkCounter(count, drink);

		Float price = order.getDeliveryCost();
		price += count * drink.getPrice();

		order.setDeliveryCost(Float.parseFloat(df.format(price).replaceAll(",", ".")));
		order.addDrink(counter);
		return true;
	}

}
