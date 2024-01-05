package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCampaign {
	private Long id;
	private CustomProduct customProduct;
	private Campaign campaign;
	private Integer quantity;
	private Money price;
	private Float weight;
	private Instant createdDate;
	private Instant modifiedDate;
}
