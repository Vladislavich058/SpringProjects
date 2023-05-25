package shop.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import shop.dao.ClientOrderRepository;
import shop.dao.SmartphoneRepository;
import shop.domain.ClientOrder;
import shop.domain.Smartphone;
import shop.domain.SmartphoneCounter;
import shop.domain.User;

@Service
public class ClientOrderServiceImpl implements ClientOrderService {

	@Autowired
	private ClientOrderRepository clientOrderRepository;

	@Autowired
	private SmartphoneRepository smartphoneRepository;

	private DecimalFormat df = new DecimalFormat("#.##");

	@Override
	public boolean checkDate(LocalDate date) {
		if (date.isBefore(LocalDate.now().plusDays(1)))
			return false;
		return true;
	}

	@Override
	public void addSmartphoneInOrder(ClientOrder clientOrder, Integer count, Long id) {
		Smartphone smartphone = smartphoneRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Smartphone not found!"));

		boolean flag = false;

		for (SmartphoneCounter counter : clientOrder.getSmartphoneCounteres()) {
			if (counter.getSmartphone().getId().equals(smartphone.getId())) {
				counter.setCount(counter.getCount() + count);
				clientOrder.setCost(Float.parseFloat(df.format(clientOrder.getCost() + smartphone.getPrice() * count)));
				flag = true;
			}
		}
		if (!flag) {
			SmartphoneCounter smartphoneCounter = SmartphoneCounter.builder().count(count).smartphone(smartphone)
					.build();
			clientOrder.setCost(Float.parseFloat(df.format(clientOrder.getCost() + smartphone.getPrice() * count)));
			clientOrder.addCount(smartphoneCounter);
		}
	}

	@Transactional
	@Override
	public ClientOrder confirmOrder(ClientOrder order, User user) {
		order.setUser(user);
		order.setStatus("actual");
		return clientOrderRepository.save(order);
	}

	@Override
	public void deleteSmartphoneFromOrder(ClientOrder clientOrder, Long id) {
		Smartphone smartphone = smartphoneRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Smartphone not found!"));
		for (int i = 0; i < clientOrder.getSmartphoneCounteres().size(); i++) {
			if (clientOrder.getSmartphoneCounteres().get(i).getSmartphone().getId().equals(id)) {
				clientOrder.setCost(Float.parseFloat(df.format((clientOrder.getCost()
						- clientOrder.getSmartphoneCounteres().get(i).getCount() * smartphone.getPrice()))));
				clientOrder.getSmartphoneCounteres().remove(i);
			}
		}
	}

	@Override
	public ClientOrder getClientOrder(Long clientOrderId) {
		return clientOrderRepository.findById(clientOrderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found!"));
	}

	@Override
	public List<ClientOrder> getClientOrders() {
		return clientOrderRepository.findAll();
	}

	@Override
	public List<ClientOrder> filterClientOrders(LocalDate fromDate, LocalDate toDate) {
		return clientOrderRepository.findAll().stream().filter(c -> (c.getDate().equals(fromDate)
				|| c.getDate().isAfter(fromDate) && (c.getDate().equals(toDate) || c.getDate().isBefore(toDate))))
				.toList();
	}

	@Override
	public List<ClientOrder> sortClientOrders(String sort) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o2.getDate().compareTo(o1.getDate());
				}
			}).toList();
		case 2:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			}).toList();
		case 3:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o1.getCost().compareTo(o2.getCost());
				}
			}).toList();
		case 4:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o2.getCost().compareTo(o1.getCost());
				}
			}).toList();
		case 5:
			return clientOrderRepository.findByStatus("actual");
		case 6:
			return clientOrderRepository.findByStatus("delivered");
		case 7:
			return clientOrderRepository.findByStatus("canceled");
		default:
			return null;
		}
	}

	@Override
	public List<ClientOrder> getClientOrdersForClient(User user) {
		return clientOrderRepository.findAll().stream().filter(o -> o.getUser().getId().equals(user.getId())).toList();
	}

	@Override
	public List<ClientOrder> findClientOrdersForClient(String find, User user) {
		return clientOrderRepository.findByIdAndUserId(Long.parseLong(find), user.getId());
	}

	@Override
	public List<ClientOrder> filterClientOrdersForClient(LocalDate fromDate, LocalDate toDate, User user) {
		return clientOrderRepository.findAll().stream()
				.filter(c -> (c.getDate().equals(fromDate) || c.getDate().isAfter(fromDate)
						&& (c.getDate().equals(toDate) || c.getDate().isBefore(toDate))))
				.filter(o -> o.getUser().getId().equals(user.getId())).toList();
	}

	@Override
	public List<ClientOrder> sortClientOrdersForClient(String sort, User user) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o2.getDate().compareTo(o1.getDate());
				}
			}).filter(o -> o.getUser().getId().equals(user.getId())).toList();
		case 2:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			}).filter(o -> o.getUser().getId().equals(user.getId())).toList();
		case 3:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o1.getCost().compareTo(o2.getCost());
				}
			}).filter(o -> o.getUser().getId().equals(user.getId())).toList();
		case 4:
			return clientOrderRepository.findAll().stream().sorted(new Comparator<ClientOrder>() {

				@Override
				public int compare(ClientOrder o1, ClientOrder o2) {
					return o2.getCost().compareTo(o1.getCost());
				}
			}).filter(o -> o.getUser().getId().equals(user.getId())).toList();
		case 5:
			return clientOrderRepository.findByStatusAndUserId("actual", user.getId());
		case 6:
			return clientOrderRepository.findByStatusAndUserId("delivered", user.getId());
		case 7:
			return clientOrderRepository.findByStatusAndUserId("canceled", user.getId());
		default:
			return null;
		}
	}

	@Override
	public void cancelOrder(Long id) {
		clientOrderRepository.findById(id).ifPresentOrElse(o -> {
			o.setStatus("canceled");
			clientOrderRepository.save(o);
		}, () -> new IllegalArgumentException("Order not found!"));
	}

	@Override
	public void confirmDelivery(Long id) {
		clientOrderRepository.findById(id).ifPresentOrElse(o -> {
			o.setStatus("delivered");
			clientOrderRepository.save(o);
		}, () -> new IllegalArgumentException("Order not found!"));
	}

	@Override
	public List<ClientOrder> findClientOrders(String find) {
		return clientOrderRepository.findOrders(find);
	}

	@Override
	public boolean checkDateInterval(LocalDate dfromDate, LocalDate toDate) {
		if (dfromDate.isAfter(toDate))
			return false;
		return true;
	}

	@Override
	public String getProfits(LocalDate date) {
		if (clientOrderRepository.getProfits(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
				LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())) != null)
			return df.format(clientOrderRepository.getProfits(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
					LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())));
		return "";
	}

	@Override
	public String getCanceledPercent(LocalDate date) {
		if (clientOrderRepository.getCanceledPercent(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
				LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())) != null)
			return df.format(
					clientOrderRepository.getCanceledPercent(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
							LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())));
		return "";
	}

	@Override
	public String getDeliveredPercent(LocalDate date) {
		if (clientOrderRepository.getDeliveredPercent(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
				LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())) != null)
			return df.format(
					clientOrderRepository.getDeliveredPercent(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
							LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength())));
		return "";
	}

	@Override
	public List<Smartphone> getMostSoldedSmartphones(LocalDate date) {
		return clientOrderRepository
				.getMostSoldedSmartphones(LocalDate.of(date.getYear(), date.getMonthValue(), 1),
						LocalDate.of(date.getYear(), date.getMonthValue(), date.getMonth().maxLength()))
				.stream().map(r -> smartphoneRepository.findById(Long.parseLong(r)).get()).limit(3).toList();
	}

}
