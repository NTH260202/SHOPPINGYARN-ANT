package com.artiexh.model.rest.marketplace.cart.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
	Set<CartCampaignResponse> campaigns;
}
