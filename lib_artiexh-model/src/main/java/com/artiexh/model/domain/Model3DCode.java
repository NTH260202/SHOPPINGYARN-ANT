package com.artiexh.model.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Model3DCode {
	TOTE_BAG(1),
	T_SHIRT(2);

	private final int value;

	public static Model3DCode fromValue(int value) {
		for (Model3DCode type : Model3DCode.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("No such value for Model3DCode: " + value);
	}

	public int getValue() {
		return value;
	}

	public Byte getByteValue() {
		return Integer.valueOf(value).byteValue();
	}
}
