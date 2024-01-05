package com.artiexh.model.rest.campaign.request;

import com.artiexh.model.domain.Money;
import com.artiexh.model.domain.ProductAttach;
import com.artiexh.model.domain.ProductType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinalizeProductRequest {
	@NotEmpty
	@Size(max = 20)
	private String productCode;
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productInCampaignId;

//	@NotNull
//	private ProductStatus status;

	@NotBlank
	@Size(max = 255)
	private String name;

	@Valid
	private Money price;

	@NotBlank
	@Size(max = 1000)
	private String description;

	@NotNull
	private ProductType type;

	@Min(0)
	private Integer maxItemsPerOrder;

//	@NotNull
//	private DeliveryType deliveryType;

//	@NotEmpty
//	private Set<PaymentMethod> paymentMethods;

	@NotNull
	@Valid
	private Set<ProductAttach> attaches;

//	@NotNull
//	@Min(0)
//	private Float weight;

//	@NotNull
//	@Min(0)
//	private Integer quantity;

	//private Set<Long> bundleItems;
}
