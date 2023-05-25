package restaurant.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Dish implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(unique = true)
	@NotBlank(message = "Name is required")
	@Size(min = 5, message = "Name must be at least 5 characters long")
	private String name;

	@NonNull
	@NotNull(message = "Kcal is required")
	@Min(value = 1, message = "Kcal should not be less than 1")
	@Max(value = 99999, message = "Kcal should not be greater than 99999")
	private Integer kcal;

	@NonNull
	@NotNull(message = "Weight is required")
	@Min(value = 1, message = "Weight should not be less than 1")
	@Max(value = 99999, message = "Weight should not be greater than 99999")
	private Integer weight;

	@NonNull
	@NotBlank(message = "Structure is required")
	@Size(min = 5, message = "Structure must be at least 5 characters long")
	private String structure;

	@NonNull
	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false)
	private Float price;

	@NonNull
	private Type type;

	public enum Type {
		SOUP, SALAD, HOT_DISH, COLD_DISH, SNACKS, DESSERT
	}
}
