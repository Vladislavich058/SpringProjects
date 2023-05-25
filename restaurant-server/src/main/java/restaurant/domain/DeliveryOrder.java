package restaurant.domain;

import restaurant.validators.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class DeliveryOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@NotBlank(message = "Street is required")
	@Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$\\s*", message = "Street must contains only letters")
	private String deliveryStreet;

	@NonNull
	@NotNull(message = "House is required")
	@Pattern(regexp = "^[1-9]{1}[0-9]*([-]{1}[1-9]{1}[0-9]*)?([-]{1}[A-Za-zА-Яа-яЁё]*)?$", message = "House must contains only letters. Enter the building of the house with a hyphen")
	private String deliveryHouse;

	@NonNull
	@NotNull(message = "Apartment is required")
	@Min(value = 1, message = "Apartment should not be less than 1")
	@Max(value = 99999, message = "Apartment should not be greater than 99999")
	private Short deliveryApartment;

	@NonNull
	@Date
	private LocalDate deliveryDate;

	@ManyToOne
	private Messenger messenger;

	@NonNull
	@Time
	private LocalTime deliveryTime;

	private LocalDateTime createdAt;

	private LocalDateTime deliveredAt;

	@NonNull
	private Float deliveryCost = 0f;

	private String deliveryComment;

	private String status;

	@NonNull
	@ManyToOne
	private Client deliveryClient;

	@NonNull
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<OrderDishCounter> orderDishCounteres = new ArrayList<>();

	@NonNull
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<OrderDrinkCounter> orderDrinkCounteres = new ArrayList<>();

	public void addDish(OrderDishCounter dish) {
		this.orderDishCounteres.add(dish);
	}

	public void addDrink(OrderDrinkCounter drink) {
		this.orderDrinkCounteres.add(drink);
	}
	
	public void deleteDish(OrderDishCounter dish) {
		this.orderDishCounteres.remove(dish);
	}

	public void deleteDrink(OrderDrinkCounter drink) {
		this.orderDrinkCounteres.remove(drink);
	}

}
