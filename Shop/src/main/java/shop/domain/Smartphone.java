package shop.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Smartphone implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String company;

	@NotNull
	private Float price;

	@NotNull
	private String photo;

	@NotNull
	private Integer cameraResolution;

	@NotNull
	private Integer frontCameraResolution;

	@NotNull
	private Integer ram;

	@NotNull
	private Integer internalMemory;

	@NotNull
	private Integer batteryCapacity;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Rate> rates = new ArrayList<Rate>();

	public void addRate(Rate rate) {
		this.rates.add(rate);
	}
	
	public void deleteRate(Rate rate) {
		this.rates.remove(rate);
	}

	public String getAverageRate() {
		DecimalFormat df = new DecimalFormat("#.##");
		int avRate = 0;
		for (Rate rate : rates) {
			avRate += rate.getStars();
		}
		return df.format((float) avRate / this.rates.size());
	}

}
