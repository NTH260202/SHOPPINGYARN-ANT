package com.artiexh.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SourceCategory {
	CAMPAIGN_SALE(1),
	CAMPAIGN_REQUEST(2),
	ORTHER(0);
	private final int value;

	public static SourceCategory fromValue(int value) {
		for (SourceCategory action : SourceCategory.values()) {
			if (action.getValue() == value) {
				return action;
			}
		}
		throw new IndexOutOfBoundsException("No such value for Source Category: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
