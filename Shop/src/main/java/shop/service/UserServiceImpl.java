package shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import shop.dao.ClientOrderRepository;
import shop.dao.RateRepository;
import shop.dao.SmartphoneRepository;
import shop.dao.UserRepository;
import shop.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RateRepository rateRepository;

	@Autowired
	private SmartphoneRepository smartphoneRepository;
	
	@Autowired
	private ClientOrderRepository clientOrderRepository;

	@Override
	public boolean checkExistUsername(String username) {
		if (userRepository.findByUsername(username).isEmpty())
			return true;
		return false;
	}

	@Override
	public User addClient(User user) {
		user.setStatus(true);
		user.setRole("client");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public boolean checkExistUsernameEdit(User editUser, User authUser) {
		if (userRepository.findByUsername(editUser.getUsername()).isPresent()
				&& !editUser.getUsername().equals(authUser.getUsername()))
			return false;
		return true;
	}

	@Override
	public User editUser(User editUser, User authUser) {
		authUser.setUsername(editUser.getUsername());
		authUser.setPassword(passwordEncoder.encode(editUser.getPassword()));
		authUser.setEmail(editUser.getEmail());
		authUser.setPhone(editUser.getPhone());
		authUser.setName(editUser.getName());
		authUser.setSurname(editUser.getSurname());
		authUser.setLastname(editUser.getLastname());

		return userRepository.save(authUser);
	}

	@Override
	public void deleteUser(Long id) {
		for (int i = 0; i < smartphoneRepository.findAll().size(); i++) {
			for (int j = 0; j < rateRepository.findByUserId(id).size(); j++) {
				if (smartphoneRepository.findAll().get(i).getRates().contains(rateRepository.findByUserId(id).get(j))) {
					smartphoneRepository.findAll().get(i).deleteRate(rateRepository.findByUserId(id).get(j));
					smartphoneRepository.save(smartphoneRepository.findAll().get(i));
				}
			}
		}
		clientOrderRepository.deleteByUserId(id);
		rateRepository.deleteByUserId(id);
		userRepository.deleteById(id);
	}

}
