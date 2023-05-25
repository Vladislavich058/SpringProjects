package restaurant.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Messenger;

public interface MessengerRepository 
			extends CrudRepository<Messenger, Long> {
	Optional<Messenger> findByStaffInfoId(Long staffInfoId);
}
