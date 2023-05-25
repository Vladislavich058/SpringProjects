package restaurant.web;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import restaurant.dao.AdminRepository;
import restaurant.dao.ClientRepository;
import restaurant.dao.DishRepository;
import restaurant.dao.DrinkRepository;
import restaurant.dao.ManagerRepository;
import restaurant.dao.MessengerRepository;
import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.Dish;
import restaurant.domain.Dish.*;
import restaurant.domain.Drink;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;
import restaurant.domain.StaffInfo;
import restaurant.domain.User;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login");
	}

	@Bean
	ApplicationRunner adminDataLoader(AdminRepository repo, PasswordEncoder passwordEncoder) {
		if (repo.findById(1).isEmpty()) {
			return args -> {
				Admin admin = Admin.builder().id(1).secretCode("123")
						.staffInfo(
								new StaffInfo((short) 5, "МР3456789", new User("admin", passwordEncoder.encode("admin"),
										"Vladislav", "Prolygin", "Petrovich", "80296494826", true)))
						.build();
				repo.save(admin);
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner managerDataLoader(ManagerRepository repo, PasswordEncoder passwordEncoder) {
		if (!repo.findAll().iterator().hasNext()) {
			return args -> {

				repo.save(new Manager("80172205467", new StaffInfo((short) 2, "КВ3455544", new User("manager",
						passwordEncoder.encode("manager"), "Valentin", "Igorev", "Viktorovich", "80296477890", true))));
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner massengerDataLoader(MessengerRepository repo, PasswordEncoder passwordEncoder) {
		if (!repo.findAll().iterator().hasNext()) {
			return args -> {

				repo.save(new Messenger("3455КВ6", new StaffInfo((short) 1, "КП2245544", new User("messenger",
						passwordEncoder.encode("messenger"), "Igor", "Igorev", "Mihalovich", "80256423490", true))));
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner clientDataLoader(ClientRepository repo, PasswordEncoder passwordEncoder) {
		if (!repo.findAll().iterator().hasNext()) {
			return args -> {

				repo.save(new Client("vova@mail.ru", new User("client", passwordEncoder.encode("client"), "Valery",
						"Erofeeva", "Aleksandrovna", "+375335544123", true)));
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner dishDataLoader(DishRepository repo, PasswordEncoder passwordEncoder) {
		if (!repo.findAll().iterator().hasNext()) {
			return args -> {

				repo.save(new Dish("Caesar salad", 234, 210,
						"Green salad, tomatoes, " + "chicken, crackers, caeser sauce, parmesan cheese, garlic, butter",
						14.5f, Type.SALAD));
				repo.save(new Dish("Tuna salad", 195, 170,
						"fried potatoes, " + "lettuce leaves, tomatoes, tuna, pickled, "
								+ "cucumbers, egg, onion, dressed on mayonnaise sauce with french mustard "
								+ "and herbs",
						15.8f, Type.SALAD));
				repo.save(new Dish("Spinach Cream soup", 230, 220,
						"Spinach, water, cream, " + "potatoes, onion, butter, dill, garlic, salt, pepper", 15.5f,
						Type.SOUP));
				repo.save(new Dish("Beef Stroganoff", 1100, 430,
						"Beef, water, sour cream, " + "flour, onion, butter, tomato paste, garlic, salt, pepper, oil",
						18.5f, Type.HOT_DISH));
				repo.save(new Dish("Rice with coconut milk", 260, 250, "Rice, coconut milk", 12.5f, Type.COLD_DISH));
				repo.save(new Dish("Beef tartare", 180, 198,
						"Beef, onion, tomatoes, sweet yellow, "
								+ "pepper, pickled capers, sunflower oil, lemon, oranges, dijon mustard, "
								+ "salt, ground black pepper",
						17.5f, Type.SNACKS));
				repo.save(new Dish("Croissant", 110, 98,
						"Sugar, wheat flour, milk, " + "chicken egg, dry yeast, butter, cinnamon", 8.5f, Type.DESSERT));
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner drinkDataLoader(DrinkRepository repo, PasswordEncoder passwordEncoder) {
		if (!repo.findAll().iterator().hasNext()) {
			return args -> {
				repo.save(new Drink("Americano", 4.5f));
				repo.save(new Drink("Espresso", 3.5f));
				repo.save(new Drink("Sparkling water", 3.0f));
			};
		}
		return null;
	}
}