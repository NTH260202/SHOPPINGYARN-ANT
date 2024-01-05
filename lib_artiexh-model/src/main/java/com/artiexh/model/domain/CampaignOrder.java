package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignOrder {
	private Long id;
	private CampaignSale campaignSale;
	private BigDecimal shippingFee;
	private String note;
	private CampaignOrderStatus status;
	private Set<OrderDetail> orderDetails;
	private Instant modifiedDate;
	private Instant createdDate;
	private List<OrderHistory> orderHistories;
	private String shippingLabel;
	private Order order;
}
