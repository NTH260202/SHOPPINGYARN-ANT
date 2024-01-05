package com.artiexh.data.jpa.entity;

import com.artiexh.data.jpa.entity.embededmodel.ProductVariantProviderId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variant_provider_mapping")
@Builder(toBuilder = true)
public class ProductVariantProviderEntity {
	@EmbeddedId
	private ProductVariantProviderId id;

	@MapsId("businessCode")
	@ManyToOne(optional = false, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "business_code", nullable = false)
	private ProviderEntity provider;

	@ManyToOne(optional=false)
	@JoinColumn(name = "variant_id", nullable = false)
	@MapsId("variantId")
	private ProductVariantEntity productVariant;

	@Column(name = "base_price_amount", nullable = false)
	private BigDecimal basePriceAmount;

	@Column(name = "manufacturing_time", nullable = false)
	private String manufacturingTime;

	@Column(name = "min_quantity", nullable = false)
	private Integer minQuantity;
}
