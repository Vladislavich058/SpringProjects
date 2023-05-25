package restaurant.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import restaurant.dao.ClientRepository;
import restaurant.dao.DeliveryOrderRepository;
import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.pdfConverter.PdfFileExporter;
import restaurant.security.ClientRegistrationForm;
import restaurant.web.DateFormatter;
import restaurant.web.ThymeMath;

@Service
public class ClientServiceImpl implements ClientService {

	private ClientRepository clientRepo;
	private DeliveryOrderRepository deliveryOrderRepo;

	private PasswordEncoder passwordEncoder;

	public ClientServiceImpl(PasswordEncoder passwordEncoder, ClientRepository clientRepo,
			DeliveryOrderRepository deliveryOrderRepo) {
		this.passwordEncoder = passwordEncoder;
		this.clientRepo = clientRepo;
		this.deliveryOrderRepo = deliveryOrderRepo;
	}

	@Override
	public ClientRegistrationForm showClientRedactForm(Client client) {
		return new ClientRegistrationForm(client.getId(),client.getUser().getUsername(), "", client.getUser().getName(),
				client.getUser().getSurname(), client.getUser().getLastname(), client.getUser().getPhone(),
				client.getEmail());
	}

	@Override
	public void deleteAccount(Client client) {
		deliveryOrderRepo.deleteByDeliveryClientId(client.getId());
		clientRepo.deleteById(client.getId());
	}

	@Override
	public void redactAccount(Client client, ClientRegistrationForm form) {
		client.getUser().setUsername(form.getUsername());
		client.getUser().setPassword(passwordEncoder.encode(form.getPassword()));
		client.getUser().setName(form.getName());
		client.getUser().setSurname(form.getSurname());
		client.getUser().setLastname(form.getLastname());
		client.getUser().setPhone(form.getPhone());

		client.setEmail(form.getMail());

		clientRepo.save(client);
	}

	@Override
	public List<DeliveryOrder> showClientOrders(Client client) {
		return deliveryOrderRepo.findAllByDeliveryClientId(client.getId());
	}

	@Override
	public void cancelClientOrder(Long id, Client client) {
		deliveryOrderRepo.findByIdAndDeliveryClientId(id, client.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		deliveryOrderRepo.findByIdAndDeliveryClientId(id, client.getId()).ifPresent(order -> {
			order.setStatus("canceled");
			deliveryOrderRepo.save(order);
		});
	}

	@Override
	public List<DeliveryOrder> findClientOrder(Long number, Client client) {
		Optional<DeliveryOrder> deliveryOrder = deliveryOrderRepo.findByIdAndDeliveryClientId(number, client.getId());

		List<DeliveryOrder> orders = new ArrayList<>();
		if (!deliveryOrder.isEmpty())
			orders.add(deliveryOrder.get());

		return orders;
	}

	@Override
	public List<DeliveryOrder> sortClientOrders(String choice, Client client) {
		List<DeliveryOrder> orders = deliveryOrderRepo.findAllByDeliveryClientId(client.getId());

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
	public List<DeliveryOrder> filterClientOrders(String dateFrom, String dateTo, Client client) {
		return deliveryOrderRepo.findDeliveryOrderByDeliveryDateBetweenAndDeliveryClientId(LocalDate.parse(dateFrom),
				LocalDate.parse(dateTo), client.getId());
	}

	@Override
	public DeliveryOrder showClientOrderView(Long id) {
		deliveryOrderRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
		return deliveryOrderRepo.findById(id).get();
	}

	@Override
	public boolean checkDateInterval(String dateFrom, String dateTo) {
		if (LocalDate.parse(dateFrom).isAfter(LocalDate.parse(dateTo)))
			return false;
		return true;
	}

	@Override
	public void print(Long id) {
		PdfFileExporter pdfFileExporter = new PdfFileExporter();

		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("order", deliveryOrderRepo.findById(id).get());
		data.put("thymeMath", new ThymeMath());
		data.put("dateFormatter", DateFormatter.getInstance());
		pdfFileExporter.exportPdfFile("check", data, "output/order_"+id+".pdf");
	}

}
