package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	private ProductInventory productInventory;
	private CampaignSale campaignSale;
	private Money price;
	private Integer quantity;
	private Integer soldQuantity;
	private BigDecimal artistProfit;
}
