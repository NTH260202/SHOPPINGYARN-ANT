package com.artiexh.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BusinessCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessCode {
	String message() default "Business code must only contain digits " +
		"and its length must be is 10 or 13 characters";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
