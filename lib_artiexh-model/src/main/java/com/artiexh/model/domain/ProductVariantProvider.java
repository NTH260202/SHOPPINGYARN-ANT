package com.artiexh.model.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductVariantProvider {
	private Long variantId;

	private String businessCode;

	private Provider provider;

	private BigDecimal basePriceAmount;

	private String manufacturingTime;

	private Integer minQuantity;
}
