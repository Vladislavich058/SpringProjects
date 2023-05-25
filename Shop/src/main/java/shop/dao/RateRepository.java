package shop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import shop.domain.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
	@Query(value="select smartphone_id from smartphone_rates\n"
			+ "inner join rate on rate.id=smartphone_rates.rates_id\n"
			+ "group by smartphone_id\n"
			+ "order by sum(stars)/count(smartphone_id) desc", nativeQuery=true)
	List<String> getRating();
	
	@Transactional
	@Modifying
	void deleteByUserId(Long id);
	
	List<Rate> findByUserId(Long id);
}
