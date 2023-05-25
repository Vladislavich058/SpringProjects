package restaurant.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javassist.expr.NewArray;
import restaurant.dao.AdminRepository;
import restaurant.dao.ClientRepository;
import restaurant.dao.DeliveryOrderRepository;
import restaurant.dao.ManagerRepository;
import restaurant.dao.MessengerRepository;
import restaurant.dao.StaffInfoRepository;
import restaurant.dao.UserRepository;
import restaurant.domain.Admin;
import restaurant.domain.Client;
import restaurant.domain.DeliveryOrder;
import restaurant.domain.Manager;
import restaurant.domain.Messenger;
import restaurant.domain.User;
import restaurant.pdfConverter.PdfFileExporter;
import restaurant.security.AdminRegistrationForm;
import restaurant.security.ClientRegistrationForm;
import restaurant.security.ManagerRegistrationForm;
import restaurant.security.MessengerRegistrationForm;
import restaurant.web.DateFormatter;
import restaurant.web.ThymeMath;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private ManagerRepository managerRepo;

	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private MessengerRepository messengerRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private StaffInfoRepository staffInfoRepo;

	@Autowired
	private DeliveryOrderRepository orderRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DeliveryOrderRepository deliveryOrderRepository;

	@Override
	public void redactAccount(Admin admin, AdminRegistrationForm form) {
		admin.getStaffInfo().getUser().setUsername(form.getUsername());
		admin.getStaffInfo().getUser().setPassword(passwordEncoder.encode(form.getPassword()));
		admin.getStaffInfo().getUser().setName(form.getName());
		admin.getStaffInfo().getUser().setSurname(form.getSurname());
		admin.getStaffInfo().getUser().setLastname(form.getLastname());
		admin.getStaffInfo().getUser().setPhone(form.getPhone());

		admin.getStaffInfo().setPassportNumber(form.getPassportNumber());
		admin.getStaffInfo().setWorkExperience(form.getWorkExperience());

		admin.setSecretCode(form.getSecretCode());

		adminRepo.save(admin);
	}

	@Override
	public void redactMessenger(MessengerRegistrationForm form) {
		messengerRepo.findById(form.getId()).ifPresent(messenger -> {
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
		});
	}

	@Override
	public void redactManager(ManagerRegistrationForm form) {
		managerRepo.findById(form.getId()).ifPresent(manager -> {
			manager.getStaffInfo().getUser().setUsername(form.getUsername());
			manager.getStaffInfo().getUser().setPassword(passwordEncoder.encode(form.getPassword()));
			manager.getStaffInfo().getUser().setName(form.getName());
			manager.getStaffInfo().getUser().setSurname(form.getSurname());
			manager.getStaffInfo().getUser().setLastname(form.getLastname());
			manager.getStaffInfo().getUser().setPhone(form.getPhone());

			manager.getStaffInfo().setPassportNumber(form.getPassportNumber());
			manager.getStaffInfo().setWorkExperience(form.getWorkExperience());

			manager.setWorkPhone(form.getWorkPhone());

			managerRepo.save(manager);
		});
	}

	@Override
	public void redactClient(ClientRegistrationForm form) {
		clientRepo.findById(form.getId()).ifPresent(client -> {
			client.getUser().setUsername(form.getUsername());
			client.getUser().setPassword(passwordEncoder.encode(form.getPassword()));
			client.getUser().setName(form.getName());
			client.getUser().setSurname(form.getSurname());
			client.getUser().setLastname(form.getLastname());
			client.getUser().setPhone(form.getPhone());

			client.setEmail(form.getMail());

			clientRepo.save(client);
		});
	}

	@Override
	public AdminRegistrationForm showAdminRedactForm(Admin admin) {
		return new AdminRegistrationForm(admin.getStaffInfo().getUser().getUsername(), "",
				admin.getStaffInfo().getUser().getName(), admin.getStaffInfo().getUser().getSurname(),
				admin.getStaffInfo().getUser().getLastname(), admin.getStaffInfo().getUser().getPhone(),
				admin.getStaffInfo().getWorkExperience(), admin.getStaffInfo().getPassportNumber(),"");
	}

	@Override
	public MessengerRegistrationForm showMessengerRedactForm(Long id) {
		Messenger messenger = messengerRepo.findById(id).get();
		return new MessengerRegistrationForm(messenger.getId(), messenger.getUsername(), "",
				messenger.getStaffInfo().getUser().getName(), messenger.getStaffInfo().getUser().getSurname(),
				messenger.getStaffInfo().getUser().getLastname(), messenger.getStaffInfo().getUser().getPhone(),
				messenger.getStaffInfo().getWorkExperience(), messenger.getStaffInfo().getPassportNumber(),
				messenger.getCarNumber());
	}

	@Override
	public ManagerRegistrationForm showManagerRedactForm(Long id) {
		Manager manager = managerRepo.findById(id).get();
		return new ManagerRegistrationForm(manager.getId(), manager.getUsername(), "",
				manager.getStaffInfo().getUser().getName(), manager.getStaffInfo().getUser().getSurname(),
				manager.getStaffInfo().getUser().getLastname(), manager.getStaffInfo().getUser().getPhone(),
				manager.getStaffInfo().getWorkExperience(), manager.getStaffInfo().getPassportNumber(),
				manager.getWorkPhone());
	}

	@Override
	public ClientRegistrationForm showClientRedactForm(Long id) {
		Client client = clientRepo.findById(id).get();
		return new ClientRegistrationForm(client.getId(), client.getUsername(), "", client.getUser().getName(),
				client.getUser().getSurname(), client.getUser().getLastname(), client.getUser().getPhone(),
				client.getEmail());
	}

	@Override
	public List<Messenger> showMessengers() {
		return (List<Messenger>) messengerRepo.findAll();
	}

	@Override
	public List<Manager> showManagers() {
		return (List<Manager>) managerRepo.findAll();
	}

	@Override
	public List<Client> showClients() {
		return (List<Client>) clientRepo.findAll();
	}

	@Override
	public void deleteMessenger(Long id) {
		messengerRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid messenger Id:" + id));
		deliveryOrderRepository.deleteByMessengerId(id);
		messengerRepo.deleteById(id);
	}

	@Override
	public void deleteManager(Long id) {
		managerRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid manager Id:" + id));
		managerRepo.deleteById(id);
	}

	@Override
	public void deleteClient(Long id) {
		clientRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + id));
		deliveryOrderRepository.deleteByDeliveryClientId(id);
		clientRepo.deleteById(id);
	}

	@Override
	public List<Messenger> findMessenger(String username) {
		Optional<Messenger> messenger = userRepo.findByUsername(username).flatMap(user -> staffInfoRepo
				.findByUserId(user.getId()).flatMap(staffInfo -> messengerRepo.findByStaffInfoId(staffInfo.getId())));

		List<Messenger> messengers = new ArrayList<>();
		if (!messenger.isEmpty())
			messengers.add(messenger.get());
		return messengers;
	}

	@Override
	public List<Manager> findManager(String username) {
		Optional<Manager> manager = userRepo.findByUsername(username).flatMap(user -> staffInfoRepo
				.findByUserId(user.getId()).flatMap(staffInfo -> managerRepo.findByStaffInfoId(staffInfo.getId())));
		List<Manager> managers = new ArrayList<>();

		if (!manager.isEmpty())
			managers.add(manager.get());
		return managers;
	}

	@Override
	public List<Client> findClient(String username) {
		Optional<Client> client = userRepo.findByUsername(username)
				.flatMap(user -> clientRepo.findByUserId(user.getId()));
		List<Client> clients = new ArrayList<>();

		if (!client.isEmpty())
			clients.add(client.get());
		return clients;
	}

	@Override
	public void changeUserStatus(Long id) {
		userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		if (userRepo.findById(id).get().getStatus()) {
			User user = userRepo.findById(id).get();
			user.setStatus(false);
			userRepo.save(user);
		} else {
			User user = userRepo.findById(id).get();
			user.setStatus(true);
			userRepo.save(user);
		}
	}

	@Override
	public boolean checkExistMessenger(MessengerRegistrationForm form) {
		if (!userRepo.findByUsername(form.getUsername()).isEmpty()
				&& !userRepo.findById(messengerRepo.findById(form.getId()).get().getStaffInfo().getUser().getId()).get()
						.getUsername().equals(form.getUsername()))
			return false;
		return true;
	}

	@Override
	public boolean checkExistManager(ManagerRegistrationForm form) {
		if (!userRepo.findByUsername(form.getUsername()).isEmpty()
				&& !userRepo.findById(managerRepo.findById(form.getId()).get().getStaffInfo().getUser().getId()).get()
						.getUsername().equals(form.getUsername()))
			return false;
		return true;
	}

	@Override
	public boolean checkExistClient(ClientRegistrationForm form) {
		if (!userRepo.findByUsername(form.getUsername()).isEmpty()
				&& !userRepo.findById(clientRepo.findById(form.getId()).get().getUser().getId()).get().getUsername()
						.equals(form.getUsername()))
			return false;
		return true;
	}

	@Override
	public List<String> getDishTop() {
		return orderRepo.getDishTop().stream().limit(3).toList();
	}

	@Override
	public List<String> getDrinkTop() {
		return orderRepo.getDrinkTop().stream().limit(3).toList();
	}

	@Override
	public Float getAveragePaycheck() {
		return orderRepo.getAveragePaycheck();
	}

	@Override
	public Float getCanceledPercent() {
		return orderRepo.getCanceledPercent();
	}

	@Override
	public Float getCompletedInTimePercent() {
		List<DeliveryOrder> orders = orderRepo.findByStatus("completed");
		int inTimeCount = 0;
		for (DeliveryOrder deliveryOrder : orders) {
			if (deliveryOrder.getDeliveredAt()
					.isBefore(LocalDateTime.of(deliveryOrder.getDeliveryDate(), deliveryOrder.getDeliveryTime()))) {
				inTimeCount++;
			}
		}

		return  ((float) inTimeCount / orderRepo.findByStatus("completed").size()) * 100;
	}

	@Override
	public Float getExpiredPercent() {
		List<DeliveryOrder> orders = orderRepo.findByStatus("completed");
		int expiredCount = 0;
		for (DeliveryOrder deliveryOrder : orders) {
			if (deliveryOrder.getDeliveredAt()
					.isBefore(LocalDateTime.of(deliveryOrder.getDeliveryDate(), deliveryOrder.getDeliveryTime()))) {
				expiredCount++;
			}
		}

		return ((float)expiredCount / orderRepo.findByStatus("completed").size()) * 100;
	}

	@Override
	public void printAnalytics() {
		PdfFileExporter pdfFileExporter = new PdfFileExporter();

		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("dishes", this.getDishTop());
		data.put("drinks", this.getDrinkTop());
		data.put("expiredPercent", this.getExpiredPercent());
		data.put("inTimePercent", this.getCompletedInTimePercent());
		data.put("canceledPercent", this.getCanceledPercent());
		data.put("payCheck", this.getAveragePaycheck());
		data.put("thymeMath", new ThymeMath());
		pdfFileExporter.exportPdfFile("analyticsPrint", data, "output/anlytics.pdf");
	}

}
