package com.artiexh.model.rest.marketplace.salecampaign.request;

import com.artiexh.model.domain.Money;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductInSaleRequest {
	@NotNull
	@Min(0)
	private Integer quantity = 0;
	private Money price;
	private BigDecimal artistProfit;
}
