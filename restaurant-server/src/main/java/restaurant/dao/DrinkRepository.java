package restaurant.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Drink;

public interface DrinkRepository 
		extends CrudRepository<Drink, Long> {
	Optional<Drink> findByName(String name);
	List<Drink> findDrinksByPriceBetween(Float priceFrom, Float priceTo);
}
