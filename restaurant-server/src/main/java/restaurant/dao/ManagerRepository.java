package restaurant.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Manager;

public interface ManagerRepository 
			extends CrudRepository<Manager, Long> {
	Optional<Manager> findByStaffInfoId(Long staffInfoId);
}
