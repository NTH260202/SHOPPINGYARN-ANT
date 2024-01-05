package com.artiexh.model.rest.order.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShippingOrderRequest {

	private BigDecimal value;

	private String note;

	private WorkShift pickWorkShift;

	private WorkShift deliverWorkShift;

	private Set<@Pattern(regexp = "1|7|10|11|13|17|18|19|20|22") Integer> tags;

	@Getter
	public enum WorkShift {
		MORNING(1),
		AFTERNOON(2),
		EVENING(3);

		private final int value;

		WorkShift(int value) {
			this.value = value;
		}
	}

}
