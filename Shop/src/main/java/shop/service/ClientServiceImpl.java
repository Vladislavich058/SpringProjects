package shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.dao.UserRepository;
import shop.domain.User;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> getClients() {
		return userRepository.findByRole("client");
	}

	@Override
	public User changeStatus(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found!"));
		if (user.getStatus()) {
			user.setStatus(false);
			return userRepository.save(user);
		}
		user.setStatus(true);
		return userRepository.save(user);
	}

	@Override
	public List<User> findClients(String find) {
		return userRepository.findClients("%" + find + "%");
	}

}
