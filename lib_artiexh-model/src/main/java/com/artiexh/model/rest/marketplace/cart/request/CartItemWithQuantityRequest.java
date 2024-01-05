package com.artiexh.model.rest.marketplace.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemWithQuantityRequest extends CartItemRequest {
	@NotNull(message = "quantity is required")
	@Min(value = 0, message = "quantity must be greater than or equal 0")
	private Integer quantity;

}
