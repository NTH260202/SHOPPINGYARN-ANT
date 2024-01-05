package com.artiexh.model.domain;

import lombok.Getter;

@Getter
public enum OrderHistoryStatus {
	CREATED(0),
	PAID(1),
	SHIPPED(2),
	DELIVERED(3),
	REFUNDED(4),
	CANCELED(-1),
	SHIPPING_FAIL(5);

	private final int value;

	OrderHistoryStatus(int value) {
		this.value = value;
	}

	public static OrderHistoryStatus from(int value) {
		return switch (value) {
			case 0 -> CREATED;
			case 1 -> PAID;
			case 2 -> SHIPPED;
			case 3 -> DELIVERED;
			case 4 -> REFUNDED;
			case -1 -> CANCELED;
			default -> throw new IndexOutOfBoundsException("No OrderHistoryStatus with value " + value + " found");
		};
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
