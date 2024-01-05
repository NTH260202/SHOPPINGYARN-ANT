package com.artiexh.model.rest.campaign.response;

import com.artiexh.model.domain.CampaignStatus;
import com.artiexh.model.domain.CampaignType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private CampaignStatus status;
	private Owner owner;
	private String name;
	private String description;
	private String thumbnailUrl;
	private CampaignType type;
	private Boolean isFinalized;
	private Instant from;
	private Instant to;
	private Creator createdBy;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long campaignSaleId;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Owner {
		@JsonSerialize(using = ToStringSerializer.class)
		private String id;
		private String username;
		private String displayName;
		private String avatarUrl;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Creator {
		@JsonSerialize(using = ToStringSerializer.class)
		private String id;
		private String username;
		private String displayName;
		private String avatarUrl;
	}
}
