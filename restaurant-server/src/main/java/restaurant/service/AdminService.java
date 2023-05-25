package restaurant.service;

import java.util.List;

import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;
import restaurant.security.AdminRegistrationForm;
import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;

public interface AdminService {

	void redactAccount(Admin admin, AdminRegistrationForm form);

	void redactMessenger(MessengerRegistrationForm form);

	void redactManager(ManagerRegistrationForm form);

	void redactClient(ClientRegistrationForm form);

	AdminRegistrationForm showAdminRedactForm(Admin admin);

	MessengerRegistrationForm showMessengerRedactForm(Long id);

	ManagerRegistrationForm showManagerRedactForm(Long id);

	ClientRegistrationForm showClientRedactForm(Long id);

	List<Messenger> showMessengers();

	List<Manager> showManagers();

	List<Client> showClients();

	void deleteMessenger(Long id);

	void deleteManager(Long id);

	void deleteClient(Long id);

	List<Messenger> findMessenger(String username);

	List<Manager> findManager(String username);

	List<Client> findClient(String username);

	void changeUserStatus(Long id);

	boolean checkExistMessenger(MessengerRegistrationForm form);

	boolean checkExistManager(ManagerRegistrationForm form);

	boolean checkExistClient(ClientRegistrationForm form);
	
	List<String> getDishTop();
	
	List<String> getDrinkTop();
	
	Float getAveragePaycheck();
	
	Float getCanceledPercent();
	
	Float getCompletedInTimePercent();
	
	Float getExpiredPercent();
	
	void printAnalytics();
}
