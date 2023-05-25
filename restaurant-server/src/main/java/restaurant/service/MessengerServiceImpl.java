package restaurant.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import restaurant.dao.DeliveryOrderRepository;
import restaurant.dao.MessengerRepository;
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Messenger;
import restaurant.security.MessengerRegistrationForm;

@Service
public class MessengerServiceImpl implements MessengerService {

	@Autowired
	private MessengerRepository messengerRepo;

	@Autowired
	private DeliveryOrderRepository deliveryOrderRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public MessengerRegistrationForm showMessengerRedactForm(Messenger messenger) {
		return new MessengerRegistrationForm(messenger.getId(),messenger.getStaffInfo().getUser().getUsername(), "",
				messenger.getStaffInfo().getUser().getName(), messenger.getStaffInfo().getUser().getSurname(),
				messenger.getStaffInfo().getUser().getLastname(), messenger.getStaffInfo().getUser().getPhone(),
				messenger.getStaffInfo().getWorkExperience(), messenger.getStaffInfo().getPassportNumber(),
				messenger.getCarNumber());
	}

	@Override
	public void deleteAccount(Messenger messenger) {
		deliveryOrderRepo.deleteByMessengerId(messenger.getId());
		messengerRepo.deleteById(messenger.getId());
	}

	@Override
	public void redactAccount(Messenger messenger, MessengerRegistrationForm form) {
		messenger.getStaffInfo().getUser().setUsername(form.getUsername());
		messenger.getStaffInfo().getUser().setPassword(passwordEncoder.encode(form.getPassword()));
		messenger.getStaffInfo().getUser().setName(form.getName());
		messenger.getStaffInfo().getUser().setSurname(form.getSurname());
		messenger.getStaffInfo().getUser().setLastname(form.getLastname());
		messenger.getStaffInfo().getUser().setPhone(form.getPhone());

		messenger.getStaffInfo().setPassportNumber(form.getPassportNumber());
		messenger.getStaffInfo().setWorkExperience(form.getWorkExperience());

		messenger.setCarNumber(form.getCarNumber());

		messengerRepo.save(messenger);
	}

	@Override
	public List<DeliveryOrder> showOrders() {
		return (List<DeliveryOrder>) deliveryOrderRepo.findByStatus("confirmed");
	}

	@Override
	public List<DeliveryOrder> showDeliveries(Messenger messenger) {
		return (List<DeliveryOrder>) deliveryOrderRepo.findByMessengerId(messenger.getId());
	}

	@Override
	public DeliveryOrder showOrderView(Long id) {
		deliveryOrderRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		return deliveryOrderRepo.findById(id).get();
	}

	@Override
	public List<DeliveryOrder> findOrder(Long id) {
		Optional<DeliveryOrder> deliveryOrder = deliveryOrderRepo.findById(id);

		List<DeliveryOrder> orders = new ArrayList<>();
		if (!deliveryOrder.isEmpty())
			if(deliveryOrder.get().getStatus().equals("confirmed"))
				orders.add(deliveryOrder.get());
		
		return orders;
	}

	@Override
	public List<DeliveryOrder> findDelivery(Long id, Messenger messenger) {
		Optional<DeliveryOrder> deliveryOrder = deliveryOrderRepo.findByIdAndMessengerId(id, messenger.getId());

		List<DeliveryOrder> orders = new ArrayList<>();
		if (!deliveryOrder.isEmpty())
			orders.add(deliveryOrder.get());
		
		return orders;
	}

	@Override
	public List<DeliveryOrder> sortOrders(String choice) {
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findAll();

		if (Integer.parseInt(choice) == 1) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					return Float.compare(o2.getDeliveryCost(), o2.getDeliveryCost());
				}
			});
		}

		if (Integer.parseInt(choice) == 2) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					if (o1.getDeliveryDate().isAfter(o2.getDeliveryDate()))
						return -1;
					if (o1.getDeliveryDate().isBefore(o2.getDeliveryDate()))
						return 1;
					if (o1.getDeliveryDate().equals(o2.getDeliveryDate()))
						return 0;
					return 1;
				}
			});
		}
		
		return orders.stream().filter(o->o.getStatus().equals("confirmed")).toList();
	}

	@Override
	public List<DeliveryOrder> sortDeliveries(String choice, Messenger messenger) {
		List<DeliveryOrder> orders = (List<DeliveryOrder>) deliveryOrderRepo.findByMessengerId(messenger.getId());

		if (Integer.parseInt(choice) == 1) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					return Float.compare(o2.getDeliveryCost(), o2.getDeliveryCost());
				}
			});
		}

		if (Integer.parseInt(choice) == 2) {
			Collections.sort(orders, new Comparator<DeliveryOrder>() {
				@Override
				public int compare(DeliveryOrder o1, DeliveryOrder o2) {
					if (o1.getDeliveryDate().isAfter(o2.getDeliveryDate()))
						return -1;
					if (o1.getDeliveryDate().isBefore(o2.getDeliveryDate()))
						return 1;
					if (o1.getDeliveryDate().equals(o2.getDeliveryDate()))
						return 0;
					return 1;
				}
			});
		}
		
		return orders;
	}

	@Override
	public List<DeliveryOrder> filterOrders(String dateFrom, String dateTo) {
		return deliveryOrderRepo.findDeliveryOrderByDeliveryDateBetween(LocalDate.parse(dateFrom),
				LocalDate.parse(dateTo)).stream().filter(o->o.getStatus().equals("confirmed")).toList();
	}

	@Override
	public List<DeliveryOrder> filterDeliveries(String dateFrom, String dateTo,  Messenger messenger) {
		return deliveryOrderRepo.findDeliveryOrderByDeliveryDateBetweenAndMessengerId(LocalDate.parse(dateFrom),
				LocalDate.parse(dateTo), messenger.getId());
	}

	@Override
	public boolean checkDateInterval(String dateFrom, String dateTo) {
		if (LocalDate.parse(dateFrom).isAfter(LocalDate.parse(dateTo)))
			return false;
		return true;
	}

	@Override
	public void confirmOrder(Long id, Messenger messenger) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setStatus("on the way");
			order.setMessenger(messengerRepo.findById(messenger.getId()).get());
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public void confirmDelivery(Long id) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setStatus("completed");
			order.setDeliveredAt(LocalDateTime.now());
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public void cancelDelivery(Long id) {
		deliveryOrderRepo.findById(id).ifPresent(order -> {
			order.setMessenger(null);
			order.setStatus("confirmed");
			deliveryOrderRepo.save(order);
		});
	}

}
