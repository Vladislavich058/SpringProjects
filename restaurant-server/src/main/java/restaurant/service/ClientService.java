package restaurant.service;

import java.util.List;

import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.security.ClientRegistrationForm;

public interface ClientService {

	ClientRegistrationForm showClientRedactForm(Client client);

	void deleteAccount(Client client);

	void redactAccount(Client client, ClientRegistrationForm form);

	List<DeliveryOrder> showClientOrders(Client client);

	void cancelClientOrder(Long id, Client client);

	List<DeliveryOrder> findClientOrder(Long number, Client client);

	List<DeliveryOrder> sortClientOrders(String choice, Client client);

	List<DeliveryOrder> filterClientOrders(String dateFrom, String dateTo, Client client);

	DeliveryOrder showClientOrderView(Long id);

	boolean checkDateInterval(String dateFrom, String dateTo);
	
	void print(Long id);
}
