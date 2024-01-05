package com.artiexh.model.rest.customproduct;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantCombinationResponse {
	private ProductOption option;
	private OptionValue optionValue;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ProductOption {
		@JsonSerialize(using = ToStringSerializer.class)
		private Long id;
		private String name;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OptionValue {
		@JsonSerialize(using = ToStringSerializer.class)
		private Long id;
		private String name;
		private String value;
	}
}
