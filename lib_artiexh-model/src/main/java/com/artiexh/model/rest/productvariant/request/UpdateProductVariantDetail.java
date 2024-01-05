package com.artiexh.model.rest.productvariant.request;

import com.artiexh.model.domain.VariantCombination;
import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductVariantDetail {
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotEmpty
	@Valid
	private Set<ProductVariantDetail.ProviderConfig> providerConfigs;

	@Valid
	private List<VariantCombination> variantCombinations;
}
