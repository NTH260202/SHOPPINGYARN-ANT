package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity {
	@EmbeddedId
	private OrderDetailId id;

	@MapsId("campaignOrderId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_order_id", nullable = false)
	private CampaignOrderEntity campaignOrder;

	@MapsId("id")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_sale_id", referencedColumnName = "campaign_sale_id", nullable = false)
	@JoinColumn(name = "product_code", referencedColumnName = "product_code", nullable = false)
	private ProductEntity product;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "price_amount")
	private BigDecimal priceAmount;

}