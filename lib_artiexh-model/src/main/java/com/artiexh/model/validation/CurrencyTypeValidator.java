package com.artiexh.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;

public class CurrencyTypeValidator implements ConstraintValidator<CurrencyType, String> {
	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		try {
			Monetary.getCurrency(s);
			return true;
		} catch (UnknownCurrencyException e) {
			return false;
		}
	}
}
