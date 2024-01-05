package com.artiexh.model.domain;

public enum ProductAttachType {
	THUMBNAIL(1),
	OTHER(2);

	private final int value;

	ProductAttachType(int value) {
		this.value = value;
	}

	public static ProductAttachType fromValue(int value) {
		for (ProductAttachType type : ProductAttachType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("No such value for ProductAttachType: " + value);
	}

	public int getValue() {
		return value;
	}
}
