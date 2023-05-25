package shop.service;

import java.util.List;

import shop.domain.Smartphone;
import shop.domain.User;

public interface RateService {
	void addRate(Long smartphoneId, Integer stars, User user);
	
	List<Smartphone> getRating();
}
