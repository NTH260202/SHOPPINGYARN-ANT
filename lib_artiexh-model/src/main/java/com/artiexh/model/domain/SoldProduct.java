package com.artiexh.model.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoldProduct {
	private String productCode;
	private Long soldQuantity;
	private Long quantity;
	private String name;
	private Money revenue;
}
