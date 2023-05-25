package shop.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shop.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByRole(String role);

	Optional<User> findByUsername(String username);

	@Query(value = "select * from user\n"
			+ "where name like ?1 or surname like ?1 or lastname like ?1 or username like ?1 or email like ?1", nativeQuery = true)
	List<User> findClients(String find);
}
