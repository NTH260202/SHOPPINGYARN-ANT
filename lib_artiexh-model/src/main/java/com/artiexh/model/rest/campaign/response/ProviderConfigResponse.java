package com.artiexh.model.rest.campaign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfigResponse {
	private String manufacturingTime;
	private Integer minQuantity;
	private BigDecimal basePriceAmount;
}
