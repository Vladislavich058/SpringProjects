package restaurant.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import restaurant.domain.OrderDishCounter;

public interface OrderDishCounterRepository 
		extends CrudRepository<OrderDishCounter, Long> {
	List<OrderDishCounter> findByDishId(Long id);
	
	@Transactional
	@Modifying
	void deleteByDishId(Long id);
}
