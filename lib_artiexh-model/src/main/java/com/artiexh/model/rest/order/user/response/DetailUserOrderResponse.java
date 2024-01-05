package com.artiexh.model.rest.order.user.response;

import com.artiexh.model.rest.transaction.OrderTransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailUserOrderResponse extends UserOrderResponse {
	private OrderTransactionResponse currentTransaction;
	private Set<UserCampaignOrderResponse> campaignOrders;
}
