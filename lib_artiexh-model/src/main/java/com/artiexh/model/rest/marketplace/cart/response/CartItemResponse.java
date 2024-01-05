package com.artiexh.model.rest.marketplace.cart.response;

import com.artiexh.model.domain.DeliveryType;
import com.artiexh.model.domain.Money;
import com.artiexh.model.domain.ProductStatus;
import com.artiexh.model.domain.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
	private String productCode;
	private ProductStatus status;
	private String name;
	private String thumbnailUrl;
	private Money price;
	private String description;
	private ProductType type;
	private Integer remainingQuantity;
	private Long maxItemsPerOrder;
	private DeliveryType deliveryType;
	private Integer quantity;
	private Float weight;
}
