package restaurant.validators;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeConstraintValidator implements ConstraintValidator<Time, LocalTime> {

	@Override
	public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
		if (value.isBefore(LocalTime.of(10, 0)) || value.isAfter(LocalTime.of(23, 0)))
			return false;
		return true;
	}

}
