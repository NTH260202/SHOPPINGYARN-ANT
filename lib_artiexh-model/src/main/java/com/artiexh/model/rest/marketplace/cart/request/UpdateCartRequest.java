package com.artiexh.model.rest.marketplace.cart.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequest {
	@NotNull(message = "items is required")
	@Valid
	private Set<CartItemWithQuantityRequest> items;
}
