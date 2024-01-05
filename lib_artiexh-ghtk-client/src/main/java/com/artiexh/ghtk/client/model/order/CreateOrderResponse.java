package com.artiexh.ghtk.client.model.order;

import com.artiexh.ghtk.client.model.GhtkResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateOrderResponse extends GhtkResponse {
	private Order order;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Order {
		private String partnerId;
		private String label;
		private String area;
		private String fee;
		private String insuranceFee;
		private String estimatedPickTime;
		private String estimatedDeliverTime;
		private Set<Product> products;
		private Integer statusId;
	}
}
