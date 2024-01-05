package com.artiexh.ghtk.client.model.order;

import com.artiexh.ghtk.client.model.GhtkResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderStatusResponse extends GhtkResponse {
	private Order order;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Order {
		private String labelId;
		private String partnerId;
		private String status;
		private String statusText;
		private String created;
		private String modified;
		private String message;
		private String pickDate;
		private String deliverDate;
		private String customerFullname;
		private String customerTel;
		private String address;
		private Integer storageDay;
		private Integer shipMoney;
		private Integer insurance;
		private Integer value;
		private Integer weight;
		private Integer pickMoney;
		private Integer isFreeship;
	}
}
