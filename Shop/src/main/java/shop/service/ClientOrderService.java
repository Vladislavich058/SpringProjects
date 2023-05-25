package shop.service;

import java.time.LocalDate;
import java.util.List;

import shop.domain.ClientOrder;
import shop.domain.Smartphone;
import shop.domain.User;

public interface ClientOrderService {
	boolean checkDate(LocalDate date);
	
	void addSmartphoneInOrder(ClientOrder clientOrder, Integer count, Long id);
	
	ClientOrder confirmOrder(ClientOrder order, User user);
	
	void deleteSmartphoneFromOrder(ClientOrder clientOrder, Long id);
	
	ClientOrder getClientOrder(Long clientOrderId);
	
	List<ClientOrder> getClientOrders();
	
	List<ClientOrder> findClientOrders(String find);
	
	List<ClientOrder> filterClientOrders(LocalDate fromDate, LocalDate toDate);
	
	List<ClientOrder> sortClientOrders(String sort);
	
	List<ClientOrder> getClientOrdersForClient(User user);
	
	List<ClientOrder> findClientOrdersForClient(String find, User user);
	
	List<ClientOrder> filterClientOrdersForClient(LocalDate fromDate, LocalDate toDate, User user);
	
	List<ClientOrder> sortClientOrdersForClient(String sort, User user);
	
	void cancelOrder(Long id);
	
	void confirmDelivery(Long id);
	
	boolean checkDateInterval(LocalDate dfromDate, LocalDate toDate);
	
	String getProfits(LocalDate date);
	
	String getCanceledPercent(LocalDate date);
	
	String getDeliveredPercent(LocalDate date);
	
	List<Smartphone> getMostSoldedSmartphones(LocalDate date);
}
