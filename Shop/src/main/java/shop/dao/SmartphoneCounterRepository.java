package shop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import jakarta.transaction.Transactional;
import shop.domain.SmartphoneCounter;

public interface SmartphoneCounterRepository extends JpaRepository<SmartphoneCounter, Long> {

	@Transactional
	@Modifying
	void deleteBySmartphoneId(Long id);
	
	List<SmartphoneCounter> findBySmartphoneId(Long id);
}
