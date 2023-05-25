package restaurant.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(Exception.class)
	public String anyOtherHandler(Model model, Exception e) {
		model.addAttribute("error", e.getMessage());
		return "error";
	}

}
