package com.artiexh.model.rest.campaign.request;

import com.artiexh.model.domain.Money;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCampaignRequest {
	@NotNull
	private Long customProductId;
	private Integer quantity;
	private Money price;
	private Float weight;
}
