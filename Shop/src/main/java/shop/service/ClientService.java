package shop.service;

import java.util.List;

import shop.domain.User;

public interface ClientService {
	List<User> getClients();
	
	User changeStatus(Long id);
	
	List<User> findClients(String find);
}
