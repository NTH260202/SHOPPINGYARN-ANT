package com.artiexh.model.rest.marketplace.cart.response;

import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartCampaignResponse {
	private SaleCampaignResponse saleCampaign;
	private Set<CartItemResponse> items;
}
