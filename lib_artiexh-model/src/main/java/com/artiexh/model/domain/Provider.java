package com.artiexh.model.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {
	private String businessCode;

	private String businessName;

	private String address;

	private String contactName;

	private String email;

	private String phone;

	private String description;

	private Set<ProductVariant> productVariants;

	private Set<ProductTemplate> productTemplates;

	private String website;

	private String imageUrl;

	private Set<ProviderCategory> categories;

	private boolean isDeleted;
}
