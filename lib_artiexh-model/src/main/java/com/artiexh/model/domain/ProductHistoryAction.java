package com.artiexh.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductHistoryAction {
	IMPORT(1),
	EXPORT(2);
	private final int value;

	public static ProductHistoryAction fromValue(int value) {
		for (ProductHistoryAction action : ProductHistoryAction.values()) {
			if (action.getValue() == value) {
				return action;
			}
		}
		throw new IndexOutOfBoundsException("No such value for Product History Action: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
