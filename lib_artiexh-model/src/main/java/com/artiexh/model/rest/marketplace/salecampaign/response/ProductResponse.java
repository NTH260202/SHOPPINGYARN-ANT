package com.artiexh.model.rest.marketplace.salecampaign.response;

import com.artiexh.model.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductResponse extends ProductInSaleCampaignResponse {
	private Money inventoryPrice;
	private Integer inventoryQuantity;
	private Integer quantity;
	private Integer soldQuantity;
	private BigDecimal artistProfit;
	private BigDecimal manufacturingPrice;
}
