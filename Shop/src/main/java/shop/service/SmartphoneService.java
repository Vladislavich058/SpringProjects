package shop.service;

import java.util.List;

import shop.domain.Smartphone;

public interface SmartphoneService {
	Smartphone addSmartphone(Smartphone smartphone);
	
	void deleteSmartphone(Long id);
	
	Smartphone editSmartphone(Smartphone smartphone);
	
	Smartphone getSmartphone(Long smartphoneId);
	
	List<Smartphone> getSmartphones();
	
	List<Smartphone> findSmartphones(String find);
	
	List<Smartphone> filterSmartphones(String fromPrice, String toPrice);
	
	List<Smartphone> sortSmartphones(String sort);
	
	boolean checkExistName(String name, Integer memory);
	
	boolean checkExistNameEdit(Smartphone smartphone);
	
	boolean checkPriceInterval(String fromPrice, String toPrice);
}
