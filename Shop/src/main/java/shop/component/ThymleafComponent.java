package shop.component;

import java.util.List;

import shop.domain.Rate;
import shop.domain.User;

public final class ThymleafComponent {
	private static ThymleafComponent instanceThymleafComponent;

	private ThymleafComponent() {
	}

	public static ThymleafComponent getInstance() {
		if (instanceThymleafComponent == null) {
			instanceThymleafComponent = new ThymleafComponent();
		}
		return instanceThymleafComponent;
	}
	
	public boolean checkExistRate(List<Rate> rates, User user) {
		for (Rate rate : rates) {
			if(rate.getUser().getId().equals(user.getId()))
				return false;
		}
		return true;
	}
}
