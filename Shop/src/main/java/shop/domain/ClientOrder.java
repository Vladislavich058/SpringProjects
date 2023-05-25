package shop.domain;

import java.io.Serializable;
import java.time.LocalDate;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String street;

	@NotNull
	private String house;

	@NotNull
	private Integer apartment;

	@NonNull
	private LocalDate date;

	@NonNull
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<SmartphoneCounter> smartphoneCounteres = new ArrayList<>();

	@NotNull
	private String status;

	@NotNull
	private Float cost=0f;

	@NotNull
	@ManyToOne
	private User user;

	public void addCount(SmartphoneCounter smartphoneCounter) {
		this.smartphoneCounteres.add(smartphoneCounter);
	}
	
	public void deleteCount(SmartphoneCounter smartphoneCounter) {
		this.smartphoneCounteres.remove(smartphoneCounter);
	}
}
