package restaurant.service;

import java.util.List;

import restaurant.domain.DeliveryOrder;
import restaurant.domain.Messenger;
import restaurant.security.MessengerRegistrationForm;

public interface MessengerService {
	MessengerRegistrationForm showMessengerRedactForm(Messenger messenger);

	void deleteAccount(Messenger messenger);

	void redactAccount(Messenger messenger, MessengerRegistrationForm form);

	List<DeliveryOrder> showOrders();

	List<DeliveryOrder> showDeliveries(Messenger messenger);

	DeliveryOrder showOrderView(Long id);

	List<DeliveryOrder> findOrder(Long id);

	List<DeliveryOrder> findDelivery(Long id, Messenger messenger);

	List<DeliveryOrder> sortOrders(String choice);

	List<DeliveryOrder> sortDeliveries(String choice,  Messenger messenger);

	List<DeliveryOrder> filterOrders(String dateFrom, String dateTo);

	List<DeliveryOrder> filterDeliveries(String dateFrom, String dateTo,  Messenger messenger);

	boolean checkDateInterval(String dateFrom, String dateTo);

	void confirmOrder(Long id, Messenger messenger);

	void confirmDelivery(Long id);

	void cancelDelivery(Long id);
}