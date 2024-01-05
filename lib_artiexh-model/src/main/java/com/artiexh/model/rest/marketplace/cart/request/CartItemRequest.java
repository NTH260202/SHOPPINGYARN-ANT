package com.artiexh.model.rest.marketplace.cart.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
	@NotNull(message = "saleCampaignId is required")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long saleCampaignId;

	@NotBlank(message = "productCode is required")
	@JsonSerialize(using = ToStringSerializer.class)
	private String productCode;
}
