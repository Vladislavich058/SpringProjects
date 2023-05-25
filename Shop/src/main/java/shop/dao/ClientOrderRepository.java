package shop.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import shop.domain.ClientOrder;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
	List<ClientOrder> findByIdAndUserId(Long id, Long userId);

	@Query(value = "select client_order.id, client_order.cost, client_order.date, client_order.status, client_order.user_id, client_order.apartment, client_order.house, client_order.street from client_order\n"
			+ "inner join user on client_order.user_id=user.id\n"
			+ "where name like ?1 or surname like ?1 or lastname like ?1 or client_order.id like ?1 ", nativeQuery = true)
	List<ClientOrder> findOrders(String find);

	List<ClientOrder> findByStatus(String status);

	List<ClientOrder> findByStatusAndUserId(String status, Long id);

	@Query(value = "select sum(cost) from client_order\n"
			+ "where status like 'delivered' and date between ?1 and ?2", nativeQuery = true)
	Float getProfits(LocalDate dateFrom, LocalDate toDate);

	@Query(value = "select (select count(*) from client_order\n"
			+ "where status like 'canceled' and date between ?1 and ?2)/count(*) * 100\n" + "from client_order\n"
			+ "where (status like 'canceled' or status like 'delivered') and date between ?1 and ?2", nativeQuery = true)
	Float getCanceledPercent(LocalDate dateFrom, LocalDate toDate);

	@Query(value = "select (select count(*) from client_order\n"
			+ "where status like 'delivered' and date between ?1 and ?2)/count(*) * 100\n" + "from client_order\n"
			+ "where (status like 'canceled' or status like 'delivered') and date between ?1 and ?2", nativeQuery = true)
	Float getDeliveredPercent(LocalDate dateFrom, LocalDate toDate);

	@Query(value = "select smartphone_id from client_order_smartphone_counteres\n"
			+ "inner join smartphone_counter on client_order_smartphone_counteres.smartphone_counteres_id = smartphone_counter.id\n"
			+ "inner join smartphone on smartphone_counter.smartphone_id=smartphone.id\n"
			+ "inner join client_order on client_order_smartphone_counteres.client_order_id = client_order.id\n"
			+ "where status like 'delivered' and date between ?1 and ?2\n" + "group by smartphone_id\n"
			+ "order by count(smartphone_id) desc", nativeQuery = true)
	List<String> getMostSoldedSmartphones(LocalDate dateFrom, LocalDate toDate);

	@Transactional
	@Modifying
	void deleteByUserId(Long id);
}
