package com.artiexh.model.rest.producttemplate.request;

import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProviderConfig {
	@NotEmpty
	private Set<String> providers;
	@NotEmpty
	private Map<String, Set<ProductVariantDetail.ProviderConfig>> providerConfigs;
}
