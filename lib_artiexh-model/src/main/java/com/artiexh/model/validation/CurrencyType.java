package com.artiexh.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyType {
	String message() default "Currency is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
