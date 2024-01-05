package com.artiexh.model.rest.marketplace.salecampaign.response;

import com.artiexh.model.domain.*;
import com.artiexh.model.rest.artist.response.OwnerResponse;
import com.artiexh.model.rest.product.response.ProductCategoryResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInSaleCampaignResponse {
	private String productCode;
	private String name;
	private String thumbnailUrl;
	private ProductStatus status;
	private Money price;
	private float averageRate;
	private ProductType type;
	private OwnerResponse owner;
	private String description;
	private Integer maxItemsPerOrder;
	private DeliveryType deliveryType;
	private Set<String> tags;
	private Set<ProductAttach> attaches;
	private ProductCategoryResponse category;
	private Set<PaymentMethod> paymentMethods;
	private Float weight;
}
