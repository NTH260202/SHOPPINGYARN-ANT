package com.artiexh.model.rest.order.response;

import com.artiexh.model.domain.DeliveryType;
import com.artiexh.model.domain.Money;
import com.artiexh.model.domain.ProductStatus;
import com.artiexh.model.domain.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
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
}
