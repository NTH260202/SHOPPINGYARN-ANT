package com.artiexh.model.domain;

public enum ProductType {
	NORMAL(1),
	MEMBER_ONLY(2),
	BUNDLE(3);

	private final int value;

	ProductType(int value) {
		this.value = value;
	}

	public static ProductType fromValue(int value) {
		for (ProductType type : ProductType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("No such value for ProductType: " + value);
	}

	public int getValue() {
		return value;
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
