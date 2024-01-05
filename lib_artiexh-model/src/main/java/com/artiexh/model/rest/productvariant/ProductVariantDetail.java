package com.artiexh.model.rest.productvariant;

import com.artiexh.model.domain.VariantCombination;
import com.artiexh.model.rest.producttemplate.ProductTemplateInfo;
import com.artiexh.model.rest.provider.ProviderInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetail {
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotEmpty
	@Valid
	private Set<ProviderConfig> providerConfigs;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Schema(allOf = ProductTemplateInfo.class)
	private ProductTemplateInfo productTemplate;

	@NotNull
	@Valid
	private List<VariantCombination> variantCombinations;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProviderConfig {

		@NotNull
		@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		private String businessCode;

		@JsonProperty(access = JsonProperty.Access.READ_ONLY)
		@Schema(allOf = ProviderInfo.class)
		private ProviderInfo provider;

		@NotNull
		private BigDecimal basePriceAmount;

		@NotBlank
		private String manufacturingTime;

		@NotNull
		@Min(1)
		private Integer minQuantity;
	}
}
