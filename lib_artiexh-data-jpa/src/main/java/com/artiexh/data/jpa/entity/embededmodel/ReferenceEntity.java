package com.artiexh.data.jpa.entity.embededmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReferenceEntity {
	CAMPAIGN_SALE(1),
	CAMPAIGN_REQUEST(2),
	ORDER(3),
	CAMPAIGN_ORDER(4),
	POST(5);
	private final int value;
	public static ReferenceEntity fromValue(int value) {
		for (ReferenceEntity action : ReferenceEntity.values()) {
			if (action.getValue() == value) {
				return action;
			}
		}
		throw new IndexOutOfBoundsException("No such value for Reference Entity: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
