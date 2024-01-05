package com.artiexh.model.rest.order.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetShippingFeeRequest {

	@NotNull
	private Long addressId;

	@NotNull
	private Integer totalWeight;

	private Integer value;

	private Set<@Pattern(regexp = "1|7|10|11|13|17|18|19|20|22") String> tags;
}
