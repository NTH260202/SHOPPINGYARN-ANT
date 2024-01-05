package com.artiexh.model.domain;

import lombok.Getter;

import java.util.Set;

@Getter
public enum CampaignStatus {
	CANCELED(-1),
	DRAFT(0),
	WAITING(1),
	APPROVED(2),
	REQUEST_CHANGE(3),
	REJECTED(4),
	MANUFACTURING(5),
	MANUFACTURED(6);

	public static final Set<CampaignStatus> ALLOWED_ADMIN_VIEW_STATUS = Set.of(
		CampaignStatus.WAITING,
		CampaignStatus.APPROVED,
		CampaignStatus.REQUEST_CHANGE,
		CampaignStatus.REJECTED,
		CampaignStatus.MANUFACTURING,
		CampaignStatus.MANUFACTURED
	);

	public static final Set<CampaignStatus> ALLOWED_PUBLISHED_STATUS = Set.of(
		CampaignStatus.APPROVED,
		CampaignStatus.MANUFACTURING,
		CampaignStatus.MANUFACTURED
	);

	public static final Set<CampaignStatus> SAVED_PROVIDER_CONFIGS_STATUS = Set.of(
		CampaignStatus.APPROVED,
		CampaignStatus.REJECTED,
		CampaignStatus.MANUFACTURING,
		CampaignStatus.MANUFACTURED
	);

	private final int value;

	CampaignStatus(int value) {
		this.value = value;
	}

	public static CampaignStatus fromValue(int value) {
		for (CampaignStatus status : CampaignStatus.values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		throw new IndexOutOfBoundsException("Unknown CampaignStatus value: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
