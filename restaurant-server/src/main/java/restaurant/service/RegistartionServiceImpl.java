package restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import restaurant.dao.ClientRepository;
import restaurant.dao.ManagerRepository;
import restaurant.dao.MessengerRepository;
import restaurant.dao.UserRepository;
import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;

@Service
public class RegistartionServiceImpl implements RegistrationService {
	
	@Autowired
	private ClientRepository clientRepo;
	
	@Autowired
	private ManagerRepository managerRepo;
	
	@Autowired
	private MessengerRepository messengerRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void saveNewClient(ClientRegistrationForm form) {
		clientRepo.save(form.toClient(passwordEncoder));
	}

	@Override
	public void saveNewManager(ManagerRegistrationForm form) {
		managerRepo.save(form.toManager(passwordEncoder));
	}

	@Override
	public void saveNewMessenger(MessengerRegistrationForm form) {
		messengerRepo.save(form.toMessenger(passwordEncoder));
	}

	@Override
	public boolean checkExistUser(String userName) {
		if(!userRepo.findByUsername(userName).isEmpty())
			return false;
		return true;
	}

}
