package com.artiexh.model.rest.campaign.response;

import com.artiexh.model.domain.Money;
import com.artiexh.model.rest.customproduct.CustomProductGeneralResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.Instant;

@Data
public class ProductInCampaignResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private Integer quantity;
	private Money price;
	private CustomProductGeneralResponse customProduct;
	private Instant createdDate;
	private Instant modifiedDate;
	private ProviderConfigResponse providerConfig;
}
