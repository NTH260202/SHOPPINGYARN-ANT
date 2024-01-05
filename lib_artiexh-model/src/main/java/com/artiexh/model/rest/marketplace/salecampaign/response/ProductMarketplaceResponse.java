package com.artiexh.model.rest.marketplace.salecampaign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMarketplaceResponse extends ProductInSaleCampaignResponse {
	private long quantity;
	private SaleCampaignResponse saleCampaign;
}
