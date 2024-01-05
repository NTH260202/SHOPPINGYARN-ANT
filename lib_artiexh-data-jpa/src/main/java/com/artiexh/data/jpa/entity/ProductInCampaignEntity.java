package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_in_campaign")
@EntityListeners(AuditingEntityListener.class)
public class ProductInCampaignEntity extends BaseAuditEntity {
	@Id
	@Tsid
	@JoinColumn(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "custom_product_id", nullable = false)
	private CustomProductEntity customProduct;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_id", nullable = false)
	private CampaignEntity campaign;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price_unit", length = 3)
	private String priceUnit;

	@Column(name = "price_amount", precision = 38, scale = 2)
	private BigDecimal priceAmount;

	@Column(name = "weight")
	private Float weight;

	@Column(name = "base_price_amount", precision = 38, scale = 2)
	private BigDecimal basePriceAmount;

	@Column(name = "manufacturing_time", length = 10)
	private String manufacturingTime;

	@Column(name = "min_quantity")
	private Integer minQuantity;

	@Lob
	@Column(name = "saved_custom_product")
	private String savedCustomProduct;

}
