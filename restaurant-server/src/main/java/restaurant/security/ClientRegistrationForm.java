package restaurant.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import restaurant.domain.Client;
import restaurant.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ClientRegistrationForm {

	private Long id;

	@NonNull
	@NotBlank(message = "Username is required")
	@Size(min = 5, message = "Username must be at least 5 characters long")
	private String username;

	@NonNull
	@NotBlank(message = "Password is required")
	@Size(min = 5, message = "Password must be at least 5 characters long")
	private String password;

	@NonNull
	@NotBlank(message = "Name is required")
	@Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Name must contains only letters")
	private String name;

	@NonNull
	@NotBlank(message = "Surname is required")
	@Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Surname must contains only letters")
	private String surname;

	@NonNull
	@NotBlank(message = "Lastname is required")
	@Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Lastname must contains only letters")
	private String lastname;

	@NonNull
	@NotBlank(message = "Phone is required")
	@Pattern(regexp = "^(80|\\+375)(\\(?(29|44|25|33)\\)?)[\\d]{7}$", message = "Incorrect phone number")
	private String phone;

	@NonNull
	@NotBlank(message = "Email is required")
	@Email(message = "Incorrect email")
	private String mail;

	public Client toClient(PasswordEncoder passwordEncoder) {
		return new Client(mail,
				new User(username, passwordEncoder.encode(password), name, surname, lastname, phone, true));
	}
}
