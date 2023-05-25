package shop.loader;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import shop.dao.UserRepository;
import shop.domain.User;

@Configuration
public class DataLoaderConfig {
	@Bean
	ApplicationRunner adminDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		if (userRepository.findByRole("admin").isEmpty()) {
			return args -> {
				userRepository.save(User.builder().username("lesha").password(passwordEncoder.encode("lesha"))
						.email("lesha@mail.ru").phone("+375296666666").name("Алексей").surname("Кабушко")
						.lastname("Викторович").role("admin").status(true).build());
			};
		}
		return null;
	}

	@Bean
	ApplicationRunner clientDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		if (userRepository.findByRole("client").isEmpty()) {
			return args -> {
				userRepository.save(User.builder().username("client").password(passwordEncoder.encode("client"))
						.email("client@gmail.com").phone("+375336648845").name("Ксения").surname("Лешкевич")
						.lastname("Валерьвена").role("client").status(true).build());
			};
		}
		return null;
	}
}
