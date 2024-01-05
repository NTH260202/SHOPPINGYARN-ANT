package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variant_combination")
@Builder(toBuilder = true)
public class ProductVariantCombinationEntity {

	@EmbeddedId
	private ProductVariantCombinationEntityId id;

	@ManyToOne()
	@JoinColumn(name = "variant_id", nullable = false)
	@MapsId("variantId")
	private ProductVariantEntity productVariant;

	@ManyToOne
	@JoinColumn(name = "option_value_id", nullable = false)
	@MapsId("option_value_id")
	private OptionValueEntity optionValue;

	@Column(name = "option_id")
	private Long optionId;
}
