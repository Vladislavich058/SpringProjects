package restaurant.service;

import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;

public interface OrderService {
	boolean addNewDishInOrder(DeliveryOrder order, Long id, Integer count);

	void deleteDishFromOrder(DeliveryOrder order, Long id);

	boolean addNewDrinkInOrder(DeliveryOrder order, Long id, Integer count);

	void deleteDrinkFromOrder(DeliveryOrder order, Long id);

	void confirmOrder(DeliveryOrder order, Client client);

	boolean checkDeliveryDate(DeliveryOrder order);
}
