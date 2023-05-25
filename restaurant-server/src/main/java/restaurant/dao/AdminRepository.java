package restaurant.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Admin;

public interface AdminRepository 
			extends CrudRepository<Admin, Integer> {
	Optional<Admin> findByStaffInfoId(Long staffInfoId);
}
