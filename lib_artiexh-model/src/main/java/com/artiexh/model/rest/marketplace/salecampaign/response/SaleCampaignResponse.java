package com.artiexh.model.rest.marketplace.salecampaign.response;

import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.domain.CampaignType;
import com.artiexh.model.rest.artist.response.OwnerResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleCampaignResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String name;
	private String description;
	private Instant publicDate;
	private Instant from;
	private Instant to;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createdBy;
	private Instant createdDate;
	private Instant modifiedDate;
	private OwnerResponse owner;
	private CampaignType type;
	private String thumbnailUrl;
	private CampaignSaleStatus status;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long campaignRequestId;
}
