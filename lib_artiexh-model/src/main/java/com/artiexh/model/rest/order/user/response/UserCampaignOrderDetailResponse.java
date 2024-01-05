package com.artiexh.model.rest.order.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCampaignOrderDetailResponse extends UserCampaignOrderResponse {
	private UserOrderResponse order;
}
