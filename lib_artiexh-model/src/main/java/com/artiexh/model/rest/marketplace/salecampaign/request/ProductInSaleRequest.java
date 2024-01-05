package com.artiexh.model.rest.marketplace.salecampaign.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInSaleRequest extends UpdateProductInSaleRequest {
	@NotNull
	private String productCode;
}
