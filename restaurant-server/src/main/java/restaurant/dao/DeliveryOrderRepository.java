package restaurant.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import restaurant.domain.DeliveryOrder;

public interface DeliveryOrderRepository 
			extends CrudRepository<DeliveryOrder, Long> {
	
	List<DeliveryOrder> findAllByDeliveryClientId(Long clientId);
	
	Optional<DeliveryOrder> findByIdAndDeliveryClientId(Long id, Long clientId);
	
	Optional<DeliveryOrder> findByIdAndMessengerId(Long id, Long messengerId);
	
	List<DeliveryOrder> findDeliveryOrderByDeliveryDateBetween(LocalDate dateFrom, LocalDate dateTo);
	
	List<DeliveryOrder> findDeliveryOrderByDeliveryDateBetweenAndDeliveryClientId(
			LocalDate dateFrom, LocalDate dateTo, Long clientId);
	
	List<DeliveryOrder> findDeliveryOrderByDeliveryDateBetweenAndMessengerId(
			LocalDate dateFrom, LocalDate dateTo, Long messengerId);
	
	List<DeliveryOrder> findByStatus(String status);
	
	List<DeliveryOrder> findByMessengerId(Long id);
	
	@Query(value = "SELECT AVG(delivery_cost) as averagePayCheck FROM delivery_order\n"
			+ "WHERE status = 'completed'", nativeQuery = true)
	Float getAveragePaycheck();
	
	@Query(value = "select (select count(*) from delivery_order\n"
			+ "				where status = 'canceled')/ count(*) * 100 as canceledPercent\n"
			+ "from delivery_order\n"
			+ "where status = 'completed' or status = 'canceled'", nativeQuery = true)
	Float getCanceledPercent();
	
	@Query(value="select name\n"
			+ "from order_dish_counter\n"
			+ "inner join dish on order_dish_counter.dish_id=dish.id\n"
			+ "inner join delivery_order_order_dish_counteres on order_dish_counter.id=delivery_order_order_dish_counteres.order_dish_counteres_id\n"
			+ "inner join delivery_order on delivery_order_order_dish_counteres.delivery_order_id=delivery_order.id\n"
			+ "where status = 'completed'\n"
			+ "group by dish_id\n"
			+ "order by sum(count) desc", nativeQuery=true)
	List<String> getDishTop();
	
	@Query(value="select name\n"
			+ "from order_drink_counter\n"
			+ "inner join drink on order_drink_counter.drink_id=drink.id\n"
			+ "inner join delivery_order_order_drink_counteres on order_drink_counter.id=delivery_order_order_drink_counteres.order_drink_counteres_id\n"
			+ "inner join delivery_order on delivery_order_order_drink_counteres.delivery_order_id=delivery_order.id\n"
			+ "where status = 'completed'\n"
			+ "group by drink_id\n"
			+ "order by sum(count) desc", nativeQuery=true)
	List<String> getDrinkTop();
	
	@Transactional
	@Modifying
	void deleteByDeliveryClientId(Long id);
	
	@Transactional
	@Modifying
	void deleteByMessengerId(Long id);
}
