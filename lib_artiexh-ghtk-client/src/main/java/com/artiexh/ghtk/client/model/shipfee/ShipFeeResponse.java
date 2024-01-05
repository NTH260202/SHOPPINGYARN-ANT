package com.artiexh.ghtk.client.model.shipfee;

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
public class ShipFeeResponse extends GhtkResponse {
	private ShipFee fee;


	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class ShipFee {
		private String name;
		private Integer fee;
		private Integer insuranceFee;
		private Integer totalFee;
	}
}
