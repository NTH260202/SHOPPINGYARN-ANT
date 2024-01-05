package com.artiexh.model.rest.product.request;

import com.artiexh.model.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

	@NotNull
	private ProductStatus status;

	@NotBlank
	@Size(max = 255)
	private String name;

	@Valid
	private Money price;

	@NotNull
	private Long categoryId;

	@NotBlank
	@Size(max = 1000)
	private String description;

	@NotNull
	private ProductType type;

	@NotNull
	@Min(0)
	private Long remainingQuantity;

	@Min(0)
	private Integer maxItemsPerOrder;

	@NotNull
	private DeliveryType deliveryType;

	@NotEmpty
	private Set<PaymentMethod> paymentMethods;

	@NotNull
	private Set<String> tags;

	@NotNull
	@Valid
	private Set<ProductAttach> attaches;

	@NotNull
	@Min(0)
	private Float weight;

	private Set<Long> bundleItems;
}
