package com.artiexh.model.domain;

import lombok.Getter;

@Getter
public enum CampaignSaleStatus {
	CLOSED(-1),
	DRAFT(0),
	ACTIVE(1);

	private final int value;

	CampaignSaleStatus(int value) {
		this.value = value;
	}

	public static CampaignSaleStatus from(int value) {
		for (CampaignSaleStatus status : CampaignSaleStatus.values()) {
			if (status.value == value) {
				return status;
			}
		}
		throw new IndexOutOfBoundsException("Invalid CampaignSaleStatus value: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
