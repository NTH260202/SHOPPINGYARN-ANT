package com.artiexh.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BusinessCodeValidator implements ConstraintValidator<BusinessCode, String> {
	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return s.length() == 10 || s.length() == 13 && s.matches("\\d+");
	}
}
