package com.artiexh.model.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType {
	GLOBAL(0),
	PRIVATE(1),
	GROUP(2);

	private final int value;

	public static NotificationType fromValue(int value) {
		for (NotificationType type : NotificationType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IllegalArgumentException("No such value for Notification Type: " + value);
	}

	public int getValue() {
		return value;
	}

	public Byte getByteValue() {
		return Integer.valueOf(value).byteValue();
	}
}
