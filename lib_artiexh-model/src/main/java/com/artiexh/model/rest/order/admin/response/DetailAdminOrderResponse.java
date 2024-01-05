package com.artiexh.model.rest.order.admin.response;

import com.artiexh.model.rest.order.user.response.UserCampaignOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailAdminOrderResponse extends AdminOrderResponse {
	private Set<UserCampaignOrderResponse> campaignOrders;
}
