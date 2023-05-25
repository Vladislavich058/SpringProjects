package restaurant.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.StaffInfo;

public interface StaffInfoRepository 
			extends CrudRepository<StaffInfo, Long> {
	Optional<StaffInfo> findByUserId(Long userId);
}
