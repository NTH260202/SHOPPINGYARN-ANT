package com.artiexh.model.domain;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Role {
	ADMIN(0),
	USER(1),
	ARTIST(2),
	STAFF(3);

	private final int value;

	public static final Set<Role> ALLOWED_STAFF_UPDATED_USER_STATUS = Set.of(USER, ARTIST);

	Role(int value) {
		this.value = value;
	}

	public static Role fromValue(int value) {
		for (Role role : Role.values()) {
			if (role.getValue() == value) {
				return role;
			}
		}
		throw new IndexOutOfBoundsException("No such value for Role: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
