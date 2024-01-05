package com.artiexh.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProvidedProductType {
	SINGLE(1), COLLECTION(2);
	private final int value;

	public static ProvidedProductType fromValue(int value) {
		for (ProvidedProductType type : ProvidedProductType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("No such value for provided product type: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
