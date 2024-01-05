package com.artiexh.model.rest.order.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutCampaign {

	@NotNull
	private Long campaignId;

	private String note;

	@NotNull
	private BigDecimal shippingFee;

	@NotEmpty
	private Set<String> itemIds;

}
