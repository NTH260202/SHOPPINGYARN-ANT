package com.artiexh.model.rest.provider;

import com.artiexh.model.rest.producttemplate.ProductTemplateInfo;
import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDetail extends ProviderInfo {

	@Size(max = 1000)
	private String description;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Set<ProductVariantDetail> providedProducts;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Set<ProductTemplateInfo> productTemplates;

	private String website;
}
