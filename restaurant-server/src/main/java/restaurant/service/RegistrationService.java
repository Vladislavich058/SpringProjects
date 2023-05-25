package restaurant.service;

import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;

public interface RegistrationService {
	void saveNewClient(ClientRegistrationForm form);

	void saveNewManager(ManagerRegistrationForm form);

	void saveNewMessenger(MessengerRegistrationForm form);
	
	boolean checkExistUser(String login);
}
