package shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.dao.RateRepository;
import shop.dao.SmartphoneRepository;
import shop.domain.Rate;
import shop.domain.Smartphone;
import shop.domain.User;

@Service
public class RateServiceImpl implements RateService {

	@Autowired
	private RateRepository rateRepository;
	
	@Autowired
	private SmartphoneRepository smartphoneRepository;
	
	@Override
	public void addRate(Long smartphoneId, Integer stars, User user) {
		smartphoneRepository.findById(smartphoneId).ifPresentOrElse(s -> {
			s.addRate(Rate.builder().stars(stars).user(user).build());
			smartphoneRepository.save(s);
		}, () -> new IllegalArgumentException("Smartphone not found!"));
	}

	@Override
	public List<Smartphone> getRating() {
		return rateRepository.getRating().stream().map(r -> smartphoneRepository.findById(Long.parseLong(r)).get())
				.limit(3).toList();
	}

}
