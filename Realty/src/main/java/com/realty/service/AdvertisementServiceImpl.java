package com.realty.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.realty.model.Advertisement;
import com.realty.model.Photo;
import com.realty.model.User;
import com.realty.repo.AdvertisementRepository;
import com.realty.repo.UserRepository;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	@Autowired
	private AdvertisementRepository advertisementRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Advertisement> getAdvertisements() {
		return advertisementRepository.findAll();
	}

	@Override
	public Advertisement getAdvertisement(Long id) {
		return advertisementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Объявление с id " + id + " не найдено!"));
	}

	@Override
	public Advertisement addAdvertisement(Advertisement advertisement, MultipartFile[] photo, User user) {
		for (int i = 0; i < photo.length; i++) {
			Path filepath = Paths.get("src/main/resources/static/images/uploads",
					Math.random() + "_" + photo[i].getOriginalFilename());
			File f = new File("/images/uploads/" + filepath.getFileName());
			while (f.exists() && !f.isDirectory()) {
				filepath = Paths.get("src/main/resources/static/images/uploads",
						Math.random() + "_" + photo[i].getOriginalFilename());
			}
			try (OutputStream os = Files.newOutputStream(filepath)) {
				os.write(photo[i].getBytes());
				Photo photoAdv = Photo.builder().image("/images/uploads/" + filepath.getFileName().toString()).build();
				advertisement.addPhoto(photoAdv);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		advertisement.setRealtor(user);
		advertisement.setCreatedDate(LocalDate.now());
		advertisement.setStatus("На обработке");
		return advertisementRepository.save(advertisement);
	}

	@Override
	public void deleteAdvertisement(Long id) {
		advertisementRepository.deleteById(id);
	}

	@Override
	public Advertisement redactAdvertisement(Long id, String price) {
		Advertisement advertisement = advertisementRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Объявление с id " + id + " не найдено!"));
		advertisement.setPrice(Float.parseFloat(price));
		advertisement.setCreatedDate(LocalDate.now());
		return advertisementRepository.save(advertisement);
	}

	@Override
	public List<Advertisement> getAdvertisementsForRealtor(Long id) {
		return advertisementRepository.findByRealtorId(id);
	}

	@Override
	public List<Advertisement> getActiveAdvertisements() {
		return advertisementRepository.findByStatus("Подтвержден");
	}

	@Override
	public List<Advertisement> findAdvertisements(String findParam) {
		return advertisementRepository.findAdvertisements(findParam + "%").stream()
				.filter(a -> a.getStatus().equals("Подтвержден")).toList();
	}

	@Override
	public List<Advertisement> filterAdvertisements(String priceFrom, String priceTo) {
		return advertisementRepository.findByPriceBetween(Float.parseFloat(priceFrom), Float.parseFloat(priceTo))
				.stream().filter(a -> a.getStatus().equals("Подтвержден")).toList();
	}

	@Override
	public List<Advertisement> sortAdvertisements(String sort) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o1.getPrice(), o2.getPrice());
				}

			}).filter(a -> a.getStatus().equals("Подтвержден")).toList();
		case 2:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o2.getPrice(), o1.getPrice());
				}

			}).filter(a -> a.getStatus().equals("Подтвержден")).toList();
		case 3:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o2.getCreatedDate().compareTo(o1.getCreatedDate());
				}

			}).filter(a -> a.getStatus().equals("Подтвержден")).toList();
		case 4:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o1.getCreatedDate().compareTo(o2.getCreatedDate());
				}

			}).filter(a -> a.getStatus().equals("Подтвержден")).toList();
		default:
			return null;
		}
	}

	@Override
	public List<Advertisement> findAdvertisementsMy(String findParam, User user) {
		return findAdvertisementsMy(findParam + "%", user);
	}

	@Override
	public List<Advertisement> filterAdvertisementsMy(String priceFrom, String priceTo, User user) {
		return advertisementRepository.findByRealtorIdAndPriceBetween(user.getId(), Float.parseFloat(priceFrom),
				Float.parseFloat(priceTo));
	}

	@Override
	public List<Advertisement> sortAdvertisementsMy(String sort, User user) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o1.getPrice(), o2.getPrice());
				}

			}).filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 2:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o2.getPrice(), o1.getPrice());
				}

			}).filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 3:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o2.getCreatedDate().compareTo(o1.getCreatedDate());
				}

			}).filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 4:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o1.getCreatedDate().compareTo(o2.getCreatedDate());
				}

			}).filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 5:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Подтвержден"))
					.filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 6:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("На обработке"))
					.filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 7:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Продано"))
					.filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		case 8:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Ошибочно"))
					.filter(a -> a.getRealtor().getId().equals(user.getId())).toList();
		default:
			return null;
		}
	}

	@Override
	public boolean checkPriceInterval(String priceFrom, String priceTo) {
		if (Float.parseFloat(priceFrom) > Float.parseFloat(priceTo))
			return false;
		return true;
	}

	@Override
	public List<Advertisement> findAdvertisementsAdmin(String findParam) {
		return advertisementRepository.findAdvertisements(findParam + "%");
	}

	@Override
	public List<Advertisement> filterAdvertisementsAdmin(String priceFrom, String priceTo) {
		return advertisementRepository.findByPriceBetween(Float.parseFloat(priceFrom), Float.parseFloat(priceTo));
	}

	@Override
	public List<Advertisement> sortAdvertisementsAdmin(String sort) {
		switch (Integer.parseInt(sort)) {
		case 1:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o1.getPrice(), o2.getPrice());
				}

			}).toList();
		case 2:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return Float.compare(o2.getPrice(), o1.getPrice());
				}

			}).toList();
		case 3:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o2.getCreatedDate().compareTo(o1.getCreatedDate());
				}

			}).toList();
		case 4:
			return advertisementRepository.findAll().stream().sorted(new Comparator<Advertisement>() {

				@Override
				public int compare(Advertisement o1, Advertisement o2) {
					return o1.getCreatedDate().compareTo(o2.getCreatedDate());
				}

			}).toList();
		case 5:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Подтвержден")).toList();
		case 6:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("На обработке"))
					.toList();
		case 7:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Продано")).toList();
		case 8:
			return advertisementRepository.findAll().stream().filter(a -> a.getStatus().equals("Ошибочно")).toList();
		default:
			return null;
		}
	}

	@Override
	public void confirmAdvertisement(Long id) {
		advertisementRepository.findById(id).ifPresentOrElse(a -> {
			a.setStatus("Подтвержден");
			advertisementRepository.save(a);
		}, () -> new IllegalArgumentException("Объявление с id " + id + " не найдено!"));

	}

	@Override
	public void cancelAdvertisement(Long id) {
		advertisementRepository.findById(id).ifPresentOrElse(a -> {
			a.setStatus("Ошибочно");
			advertisementRepository.save(a);
		}, () -> new IllegalArgumentException("Объявление с id " + id + " не найдено!"));
	}

	@Override
	public List<User> getTopRealtor() {
		return advertisementRepository.getTopRealtor().stream().map(s -> userRepository.findByUsername(s).get())
				.toList();
	}

}
