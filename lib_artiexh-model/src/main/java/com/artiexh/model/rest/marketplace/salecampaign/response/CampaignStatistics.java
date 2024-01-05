package com.artiexh.model.rest.marketplace.salecampaign.response;

import com.artiexh.model.domain.Money;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignStatistics {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long campaignId;
	private String name;
	private Instant from;
	private Instant to;
	private Money revenue;
	private Money profit;
	private ProductStatisticResponse bestSoldProduct;
	private ProductStatisticResponse worstSoldProduct;
	private List<ProductStatisticResponse> products;
}
