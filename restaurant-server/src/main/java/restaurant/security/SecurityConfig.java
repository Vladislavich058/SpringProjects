package restaurant.security;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import restaurant.dao.AdminRepository;
import restaurant.dao.ClientRepository;
import restaurant.dao.ManagerRepository;
import restaurant.dao.MessengerRepository;
import restaurant.dao.StaffInfoRepository;
import restaurant.dao.UserRepository;
import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;
import restaurant.domain.User;
import restaurant.security.SecurityComponent.LoginSuccessHandler;

@Configuration
public class SecurityConfig {

	private LoginSuccessHandler authenticationSuccessHandler;

	public SecurityConfig(LoginSuccessHandler authenticationSuccessHandler) {
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepo, StaffInfoRepository staffInfoRepo,
			AdminRepository adminRepo, ClientRepository clientRepo, ManagerRepository managerRepo,
			MessengerRepository messengerRepo) {
		return username -> {
			Optional<User> user = userRepo.findByUsername(username);

			if (!user.isEmpty()) {

				Optional<Client> client = clientRepo.findByUserId(user.get().getId());
				if (!client.isEmpty())
					return client.get();

				Optional<Admin> admin = adminRepo
						.findByStaffInfoId(staffInfoRepo.findByUserId(user.get().getId()).get().getId());
				if (!admin.isEmpty())
					return admin.get();

				Optional<Manager> manager = managerRepo
						.findByStaffInfoId(staffInfoRepo.findByUserId(user.get().getId()).get().getId());
				if (!manager.isEmpty())
					return manager.get();

				Optional<Messenger> messenger = messengerRepo
						.findByStaffInfoId(staffInfoRepo.findByUserId(user.get().getId()).get().getId());
				if (!messenger.isEmpty())
					return messenger.get();
			}

			throw new UsernameNotFoundException("User ‘" + username + "’ not found");

		};
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.formLogin(login -> login.loginPage("/login").successHandler(authenticationSuccessHandler)
                .failureUrl("/login-error")).authorizeHttpRequests().requestMatchers("/admin", "/admin/**")
                .hasRole("ADMIN").requestMatchers("/manager", "/manager/**").hasRole("MANAGER")
                .requestMatchers("/order", "/client", "/client/**").hasRole("CLIENT").requestMatchers("/", "/**")
                .permitAll().and().logout(logout -> logout.logoutSuccessUrl("/")).build();
	}
}
