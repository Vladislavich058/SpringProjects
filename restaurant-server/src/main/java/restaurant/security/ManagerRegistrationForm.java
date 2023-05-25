package restaurant.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import restaurant.domain.Manager;
import restaurant.domain.StaffInfo;
import restaurant.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ManagerRegistrationForm {

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
	@NotNull(message = "Work experience is required")
	@Min(value = 1, message = "Work experience should not be less than 1")
	@Max(value = 99, message = "Work experience should not be greater than 99")
	private Short workExperience;

	@NonNull
	@NotBlank(message = "Passport number is required")
	@Pattern(regexp = "^[А-Я]{2}[\\d]{7}$", message = "Incorrect passport number")
	private String passportNumber;

	@NonNull
	@NotBlank(message = "Work Phone is required")
	@Pattern(regexp = "^(80|\\+375)(\\(?(17)\\)?)[\\d]{7}$", message = "Use city code: 8017 or +37517")
	private String workPhone;

	public Manager toManager(PasswordEncoder passwordEncoder) {
		return new Manager(workPhone, new StaffInfo(workExperience, passportNumber,
				new User(username, passwordEncoder.encode(password), name, surname, lastname, phone, true)));
	}
}
