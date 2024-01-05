package com.artiexh.model.rest.order.user.response;

import com.artiexh.model.domain.OrderHistory;
import com.artiexh.model.rest.order.response.OrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCampaignOrderResponse extends CampaignOrderResponsePage {
	private Set<OrderDetailResponse> orderDetails;
	private BigDecimal shippingFee;
	private List<OrderHistory> orderHistories;
	private String shippingLabel;
}
