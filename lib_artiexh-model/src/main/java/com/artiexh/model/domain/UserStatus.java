package com.artiexh.model.domain;

import lombok.Getter;

import java.util.Set;

@Getter
public enum UserStatus {
	BANNED(-1),
	INACTIVE(0),
	ACTIVE(1);


	private final int value;

	UserStatus(int value) {
		this.value = value;
	}

	public static UserStatus fromValue(int value) {
		for (UserStatus status : UserStatus.values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		throw new IndexOutOfBoundsException("No such value for UserStatus: " + value);
	}

	public Byte getByteValue() {
		return (byte) value;
	}
}
