package com.artiexh.model.domain;

public enum DeliveryType {
	SHIP(1),
	AT_EVENT(2);

	private final int value;

	DeliveryType(int value) {
		this.value = value;
	}

	public static DeliveryType fromValue(int value) {
		for (DeliveryType type : DeliveryType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("No such value for DeliveryType: " + value);
	}

	public int getValue() {
		return value;
	}
}
