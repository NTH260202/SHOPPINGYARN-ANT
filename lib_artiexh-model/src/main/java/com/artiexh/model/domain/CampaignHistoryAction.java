package com.artiexh.model.domain;

import lombok.Getter;

@Getter
public enum CampaignHistoryAction {
	CREATE(1),
	SUBMIT(2),
	REQUEST_CHANGE(3),
	APPROVE(4),
	REJECT(5),
	CANCEL(6),
	MANUFACTURING(7),
	MANUFACTURED(8),
	PUBLISHED(9);

	private final int value;

	CampaignHistoryAction(int value) {
		this.value = value;
	}

	public static CampaignHistoryAction fromValue(int value) {
		return switch (value) {
			case 1 -> CREATE;
			case 2 -> SUBMIT;
			case 3 -> REQUEST_CHANGE;
			case 4 -> APPROVE;
			case 5 -> REJECT;
			case 6 -> CANCEL;
			case 7 -> MANUFACTURING;
			case 8 -> MANUFACTURED;
			case 9 -> PUBLISHED;
			default -> throw new IndexOutOfBoundsException("Unknown CampaignHistoryAction value: " + value);
		};
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
