package restaurant.validators;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = DateConstraintValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {

	String message() default "Invalid date";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
