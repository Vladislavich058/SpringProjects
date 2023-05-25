package restaurant.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import restaurant.domain.OrderDrinkCounter;

public interface OrderDrinkCounterRepository 
		extends CrudRepository<OrderDrinkCounter, Long> {
	List<OrderDrinkCounter> findByDrinkId(Long id);
	
	@Transactional
	@Modifying
	void deleteByDrinkId(Long id);
}
