package shop.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.dao.ClientOrderRepository;
import shop.dao.SmartphoneCounterRepository;
import shop.dao.SmartphoneRepository;
import shop.domain.Smartphone;

@Service
public class SmartphoneServiceImpl implements SmartphoneService {

	@Autowired
	private SmartphoneRepository smartphoneRepository;

	@Autowired
	private SmartphoneCounterRepository smartphoneCounterRepository;

	@Autowired
	private ClientOrderRepository clientOrderRepository;

	@Override
	public Smartphone addSmartphone(Smartphone smartphone) {
		return smartphoneRepository.save(smartphone);
	}

	@Override
	public void deleteSmartphone(Long id) {
		for (int i = 0; i < clientOrderRepository.findAll().size(); i++) {
			for (int j = 0; j < smartphoneCounterRepository.findBySmartphoneId(id).size(); j++) {
				if (clientOrderRepository.findAll().get(i).getSmartphoneCounteres()
						.contains(smartphoneCounterRepository.findBySmartphoneId(id).get(j))) {
					clientOrderRepository.findAll().get(i)
							.deleteCount(smartphoneCounterRepository.findBySmartphoneId(id).get(j));
					clientOrderRepository.save(clientOrderRepository.findAll().get(i));
				}
			}
		}
		smartphoneCounterRepository.deleteBySmartphoneId(id);
		smartphoneRepository.deleteById(id);
	}

	@Override
	public Smartphone editSmartphone(Smartphone smartphone) {
		Smartphone redSmartphone = smartphoneRepository.findById(smartphone.getId())
				.orElseThrow(() -> new IllegalArgumentException("Smartphone not found!"));
		redSmartphone.setName(smartphone.getName());
		redSmartphone.setCompany(smartphone.getCompany());
		redSmartphone.setRam(smartphone.getRam());
		redSmartphone.setInternalMemory(smartphone.getInternalMemory());
		redSmartphone.setBatteryCapacity(smartphone.getBatteryCapacity());
		redSmartphone.setCameraResolution(smartphone.getCameraResolution());
		redSmartphone.setFrontCameraResolution(smartphone.getFrontCameraResolution());
		redSmartphone.setPhoto(smartphone.getPhoto());
		redSmartphone.setPrice(smartphone.getPrice());
		return smartphoneRepository.save(redSmartphone);
	}

	@Override
	public List<Smartphone> getSmartphones() {
		return smartphoneRepository.findAll();
	}

	@Override
	public List<Smartphone> findSmartphones(String find) {
		return smartphoneRepository.findSmartphones("%" + find + "%");
	}

	@Override
	public List<Smartphone> filterSmartphones(String fromPrice, String toPrice) {
		return smartphoneRepository.findAll().stream()
				.filter(p -> p.getPrice() >= Float.parseFloat(fromPrice) && p.getPrice() <= Float.parseFloat(toPrice))
				.toList();
	}

	@Override
	public List<Smartphone> sortSmartphones(String sort) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o1.getName().compareTo(o2.getName());
				}
			}).toList();
		case 2:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o2.getName().compareTo(o1.getName());
				}
			}).toList();
		case 3:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o1.getPrice().compareTo(o2.getPrice());
				}
			}).toList();
		case 4:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o2.getPrice().compareTo(o1.getPrice());
				}
			}).toList();
		case 5:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o1.getRam().compareTo(o2.getRam());
				}
			}).toList();
		case 6:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o2.getRam().compareTo(o1.getRam());
				}
			}).toList();
		case 7:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o1.getInternalMemory().compareTo(o2.getInternalMemory());
				}
			}).toList();
		case 8:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o2.getInternalMemory().compareTo(o1.getInternalMemory());
				}
			}).toList();
		case 9:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o1.getBatteryCapacity().compareTo(o2.getBatteryCapacity());
				}
			}).toList();
		case 10:
			return smartphoneRepository.findAll().stream().sorted(new Comparator<Smartphone>() {

				@Override
				public int compare(Smartphone o1, Smartphone o2) {
					return o2.getBatteryCapacity().compareTo(o1.getBatteryCapacity());
				}
			}).toList();
		default:
			return null;
		}
	}

	@Override
	public boolean checkExistName(String name, Integer memory) {
		if (smartphoneRepository.findByNameAndInternalMemory(name, memory).isEmpty())
			return true;
		return false;
	}

	@Override
	public boolean checkExistNameEdit(Smartphone smartphone) {
		if (smartphoneRepository.findByNameAndInternalMemory(smartphone.getName(), smartphone.getInternalMemory())
				.isPresent()
				&& (!smartphoneRepository.findById(smartphone.getId()).get().getName().equals(smartphone.getName())
						|| smartphoneRepository.findById(smartphone.getId()).get().getInternalMemory() != smartphone
								.getInternalMemory()))
			return false;
		return true;
	}

	@Override
	public boolean checkPriceInterval(String fromPrice, String toPrice) {
		if (Float.parseFloat(toPrice) < Float.parseFloat(fromPrice))
			return false;
		return true;
	}

	@Override
	public Smartphone getSmartphone(Long smartphoneId) {
		return smartphoneRepository.findById(smartphoneId)
				.orElseThrow(() -> new IllegalArgumentException("Smartphone not found!"));
	}

}
