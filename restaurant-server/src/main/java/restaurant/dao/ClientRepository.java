package restaurant.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Client;

public interface ClientRepository 
			extends CrudRepository<Client, Long> {
	Optional<Client> findByUserId(Long userId);
}
