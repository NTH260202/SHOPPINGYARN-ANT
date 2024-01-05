package com.artiexh.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentMethod {
	CASH(1),
	VN_PAY(2);
	private final int value;

	public static PaymentMethod fromValue(int value) {
		for (PaymentMethod paymentMethod : PaymentMethod.values()) {
			if (paymentMethod.getValue() == value) {
				return paymentMethod;
			}
		}
		throw new IndexOutOfBoundsException("No such value for payment method: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
