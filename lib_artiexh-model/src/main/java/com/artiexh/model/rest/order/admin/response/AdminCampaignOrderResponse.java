package com.artiexh.model.rest.order.admin.response;

import com.artiexh.model.domain.OrderHistory;
import com.artiexh.model.rest.order.response.OrderDetailResponse;
import com.artiexh.model.rest.order.user.response.AdminCampaignOrderResponsePage;
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
public class AdminCampaignOrderResponse extends AdminCampaignOrderResponsePage {
	private List<OrderHistory> orderHistories;
	private AdminOrderResponse order;
}
