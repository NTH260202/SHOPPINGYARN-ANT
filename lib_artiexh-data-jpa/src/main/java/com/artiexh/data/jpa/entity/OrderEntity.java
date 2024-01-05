package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity extends BaseAuditEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "payment_method", nullable = false)
	private Byte paymentMethod;

	@Column(name = "delivery_address")
	private String deliveryAddress;

	@Column(name = "delivery_ward")
	private String deliveryWard;

	@Column(name = "delivery_district")
	private String deliveryDistrict;

	@Column(name = "delivery_province")
	private String deliveryProvince;

	@Column(name = "delivery_country")
	private String deliveryCountry;

	@Column(name = "delivery_tel", length = 15)
	private String deliveryTel;

	@Column(name = "delivery_email", length = 254)
	private String deliveryEmail;

	@Column(name = "pick_address")
	private String pickAddress;

	@Column(name = "pick_ward")
	private String pickWard;

	@Column(name = "pick_district")
	private String pickDistrict;

	@Column(name = "pick_province")
	private String pickProvince;

	@Column(name = "pick_country")
	private String pickCountry;

	@Column(name = "pick_tel")
	private String pickTel;

	@Column(name = "pick_name")
	private String pickName;

	@Column(name = "pick_email", length = 254)
	private String pickEmail;

	@Column(name = "return_address")
	private String returnAddress;

	@Column(name = "return_ward")
	private String returnWard;

	@Column(name = "return_district")
	private String returnDistrict;

	@Column(name = "return_province")
	private String returnProvince;

	@Column(name = "return_country")
	private String returnCountry;

	@Column(name = "return_tel", length = 15)
	private String returnTel;

	@Column(name = "return_name")
	private String returnName;

	@Column(name = "return_email")
	private String returnEmail;

	@Builder.Default
	@OneToMany(mappedBy = "order")
	private Set<CampaignOrderEntity> campaignOrders = new LinkedHashSet<>();

	@Builder.Default
	@OneToMany
	@JoinColumn(name = "order_id")
	private Set<OrderTransactionEntity> orderTransactions = new LinkedHashSet<>();

	@Size(max = 255)
	@Column(name = "delivery_name")
	private String deliveryName;

	@Builder.Default
	@Column(name = "status", nullable = false)
	private Byte status = 0;

}