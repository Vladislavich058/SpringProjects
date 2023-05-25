package shop.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.domain.Smartphone;

public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {
	Optional<Smartphone> findByNameAndInternalMemory(String name, Integer memory);
	
	@Query(value = "select * from smartphone\n"
			+ "where name like ?1 or company like ?1", nativeQuery = true)
	List<Smartphone> findSmartphones(String find);
}
