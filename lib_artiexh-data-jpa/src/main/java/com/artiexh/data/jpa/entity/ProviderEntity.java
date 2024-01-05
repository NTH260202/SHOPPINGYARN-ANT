package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "provider")
public class ProviderEntity {
	@Id
	@Column(name = "business_code", nullable = false, length = 13)
	private String businessCode;

	@Column(name = "business_name", nullable = false)
	private String businessName;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "contact_name", nullable = false)
	private String contactName;

	@Column(name = "email", length = 254)
	private String email;

	@Column(name = "phone", length = 15, nullable = false)
	private String phone;

	@Column(name = "description", length = 1000, nullable = false)
	private String description;

	@OneToMany(mappedBy = "provider", fetch = FetchType.EAGER)
	private Set<ProductVariantProviderEntity> productVariantConfigs;

	@Column(name = "website")
	private String website;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@ManyToMany(mappedBy = "providers")
	private Set<ProductTemplateEntity> productTemplates;

	@Builder.Default
	@ManyToMany
	@JoinTable(name = "provider_category_mapping",
		joinColumns = @JoinColumn(name = "provider_id"),
		inverseJoinColumns = @JoinColumn(name = "provider_category_id"))
	private Set<ProviderCategoryEntity> categories = new LinkedHashSet<>();

	@Column(name = "is_deleted", nullable = false)
	@Builder.Default
	private boolean isDeleted = false;
}
