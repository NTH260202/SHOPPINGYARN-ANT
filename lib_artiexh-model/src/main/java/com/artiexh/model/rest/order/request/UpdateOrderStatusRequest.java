package com.artiexh.model.rest.order.request;

import com.artiexh.model.domain.CampaignOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
	private String message;
	@NotNull
	private CampaignOrderStatus status;
}
