package restaurant.service;

import java.util.List;

import restaurant.domain.DeliveryOrder;
import restaurant.domain.Dish;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.security.ManagerRegistrationForm;

public interface ManagerService {
	ManagerRegistrationForm showManagerRedactForm(Manager manager);

	Dish showDishRedact(Long id);

	Drink showDrinkRedact(Long id);

	DeliveryOrder showOrderView(Long id);

	void deleteAccount(Manager manager);

	void redactAccount(Manager manager, ManagerRegistrationForm form);

	List<Dish> showDishes();

	List<Drink> showDrinks();

	List<DeliveryOrder> showOrders();

	List<DeliveryOrder> showDeliveries();

	void deleteDish(Long id);

	void deleteDrink(Long id);

	void deleteOrder(Long id);

	void cancelOrder(Long id);

	void cancelDelivery(Long id);

	void confirmOrder(Long id);

	void addDish(Dish dish);

	void addDrink(Drink drink);

	void redactDish(Dish dish);

	void redactDrink(Drink drink);

	List<Dish> findDish(String name);

	List<Drink> findDrink(String name);

	List<DeliveryOrder> findOrder(Long number);

	List<DeliveryOrder> findDelivery(Long number);

	List<Dish> sortDish(String choice);

	List<Drink> sortDrink();

	List<DeliveryOrder> sortOrders(String choice);

	List<DeliveryOrder> sortDeliveries(String choice);

	List<Dish> filterDish(String priceFrom, String priceTo);

	List<Drink> filterDrink(String priceFrom, String priceTo);

	List<DeliveryOrder> filterOrders(String dateFrom, String dateTo);

	List<DeliveryOrder> filterDeliveries(String dateFrom, String dateTo);

	boolean checkExistDishForAdd(Dish dish);

	boolean checkExistDrinkForAdd(Drink drink);

	boolean checkExistDishForRedact(Dish dish);

	boolean checkExistDrinkForRedact(Drink drink);

	boolean checkPriceInterval(String priceFrom, String priceTo);

	boolean checkDateInterval(String dateFrom, String dateTo);
	
	void print(Long id);
}
