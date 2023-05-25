package restaurant.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import restaurant.dao.DishRepository;
import restaurant.domain.Dish;

@Component
public class DishByIdConverter implements Converter<Long, Dish> {

	private DishRepository dishRepo;

	public DishByIdConverter(DishRepository dishRepo) {
		this.dishRepo = dishRepo;
	}

	@Override
	public Dish convert(Long id) {
		return dishRepo.findById(id).orElse(null);
	}
}
