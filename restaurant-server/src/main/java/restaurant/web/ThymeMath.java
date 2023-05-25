package restaurant.web;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component
public class ThymeMath {
	public String ceil(Float a) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(a);
	}
}
