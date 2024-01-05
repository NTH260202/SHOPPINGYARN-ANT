package com.artiexh.model.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
	CLOSED(-1),
	PAYING(0),
	PAID(1);

	private final int value;

	OrderStatus(int value) {
		this.value = value;
	}

	public static OrderStatus fromValue(int value) {
		return switch (value) {
			case -1 -> CLOSED;
			case 0 -> PAYING;
			case 1 -> PAID;
			default -> throw new IndexOutOfBoundsException("No such value for order status: " + value);
		};
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
