package restaurant.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import restaurant.domain.Dish;
import restaurant.domain.Dish.Type;

public interface DishRepository 
			extends CrudRepository<Dish, Long> {
	Optional<Dish> findByName(String name);
	List<Dish> findDishesByPriceBetween(Float priceFrom, Float priceTo);
	List<Dish> findByType(Type type);
}
