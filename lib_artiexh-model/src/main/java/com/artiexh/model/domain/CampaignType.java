package com.artiexh.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum CampaignType {
	SHARE(1),
	PUBLIC(2),
	PRIVATE(3);

	private final int value;

	public static final Set<CampaignType> MARKETPLACE_VIEW_TYPE = Set.of(
		CampaignType.SHARE,
		CampaignType.PUBLIC
	);

	public static final Set<CampaignType> ARTIST_VIEW_TYPE = Set.of(
		CampaignType.SHARE,
		CampaignType.PRIVATE
	);

	public static CampaignType fromValue(int value) {
		for (CampaignType type : CampaignType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		throw new IndexOutOfBoundsException("Unknown Campaign Type value: " + value);
	}

	public byte getByteValue() {
		return (byte) value;
	}
}
