package shop.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import shop.dao.UserRepository;
import shop.domain.User;
import shop.security.SuccessHandlerConfig.LoginSuccessHandler;

@Configuration
public class SecurityConfig {

	@Autowired
	private LoginSuccessHandler authenticationSuccessHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepo) {
		return username -> {
			Optional<User> user = userRepo.findByUsername(username);

			if (!user.isEmpty()) {
				return user.get();
			}

			throw new UsernameNotFoundException("User not found!");

		};
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.formLogin(login -> login.loginPage("/login").successHandler(authenticationSuccessHandler)
						.failureUrl("/errorMes"))
				.authorizeHttpRequests().requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
				.requestMatchers("/client", "/client/**").hasRole("CLIENT").requestMatchers("/", "/**").permitAll()
				.and().logout(logout -> logout.logoutSuccessUrl("/")).build();
	}
}
