package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class ProductVariantCombinationEntityId implements Serializable {

	@Column(name = "variant_id", nullable = false)
	private Long variantId;
	@Column(name = "option_value_id")
	private Long optionValueId;
}
