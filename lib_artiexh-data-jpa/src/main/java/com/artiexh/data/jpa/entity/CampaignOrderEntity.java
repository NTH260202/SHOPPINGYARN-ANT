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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campaign_order")
@EntityListeners(AuditingEntityListener.class)
public class CampaignOrderEntity extends BaseAuditEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_id", nullable = false)
	private CampaignSaleEntity campaignSale;

	@Column(name = "note")
	private String note;

	@Column(name = "status", nullable = false)
	private Byte status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	@Column(name = "shipping_fee", nullable = false, precision = 38, scale = 2)
	private BigDecimal shippingFee;

	@Column(name = "shipping_label")
	private String shippingLabel;

	@OneToMany(mappedBy = "campaignOrder")
	private Set<OrderDetailEntity> orderDetails = new LinkedHashSet<>();

	@OneToMany
	@JoinColumn(name = "campaign_order_id")
	private Set<OrderHistoryEntity> orderHistories = new LinkedHashSet<>();

}