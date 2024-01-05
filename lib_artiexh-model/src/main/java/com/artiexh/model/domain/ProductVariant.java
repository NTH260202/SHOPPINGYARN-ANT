package com.artiexh.model.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {
	private Long id;

	private Long productTemplateId;

	private ProductTemplate productTemplate;

	private Set<VariantCombination> variantCombinations;

	private Set<ProductVariantProvider> providerConfigs;
}
