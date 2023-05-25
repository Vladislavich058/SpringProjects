package restaurant.validators;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateConstraintValidator implements ConstraintValidator<Date, LocalDate> {

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
		if (value.equals(LocalDate.now()) || value.isAfter(LocalDate.now()))
			return true;
		return false;
	}

}
