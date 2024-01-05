package com.artiexh.model.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventory {
	private String productCode;
	private Artist owner;
	private ProductStatus status;
	private String name;
	private String thumbnailUrl;
	private Money price;
	private ProductCategory category;
	private String description;
	private ProductType type;
	private Long quantity;
	private Long maxItemsPerOrder;
	private DeliveryType deliveryType;
	private Float averageRate;
	private Set<Byte> paymentMethods;
	private Set<ProductTag> tags;
	private Set<ProductAttach> attaches;
	private Float weight;
	//	private Set<Product> bundles;
//	private Set<Product> bundleItems;
	private ProductInCampaign productInCampaign;
	private BigDecimal manufacturingPrice;
	private Instant modifiedDate;
	private Instant createdDate;
}
