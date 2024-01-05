package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_inventory")
@EntityListeners(AuditingEntityListener.class)
public class ProductInventoryEntity extends BaseAuditEntity {
	@Id
	@Column(name = "product_code", length = 20)
	private String productCode;

	@OneToOne(orphanRemoval = true,
		fetch = FetchType.LAZY,
		cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "product_in_campaign_id")
	private ProductInCampaignEntity productInCampaign;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "owner_id", nullable = false)
	private ArtistEntity owner;

	@Column(name = "status", nullable = false)
	private Byte status;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "price_amount", nullable = false)
	private BigDecimal priceAmount;

	@Column(name = "price_unit", nullable = false, length = 3)
	private String priceUnit;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "category_id", nullable = false)
	private ProductCategoryEntity category;

	@Column(name = "description")
	private String description;

	@Column(name = "type", nullable = false)
	private Byte type;

	@Column(name = "quantity", columnDefinition = "INT UNSIGNED not null")
	private Long quantity;

	@Column(name = "max_items_per_order", columnDefinition = "INT UNSIGNED")
	private Long maxItemsPerOrder;

	@Column(name = "delivery_type", nullable = false)
	private Byte deliveryType;

	@Builder.Default
	@Column(name = "average_rate", nullable = false)
	private Float averageRate = 0f;

	@Column(name = "payment_method")
	@Lob
	private Byte[] paymentMethods;

	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "product_tag_mapping",
		joinColumns = @JoinColumn(name = "product_code"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<ProductTagEntity> tags;

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "product_id")
	private Set<ProductAttachEntity> attaches;

	@Builder.Default
	@Column(name = "weight", nullable = false)
	private Float weight = 500F;

	@Column(name = "manufacturing_price", precision = 38, scale = 2)
	private BigDecimal manufacturingPrice;

	@Column(name = "is_deleted")
	private boolean isDeleted;

//	@Builder.Default
//	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//	@JoinTable(name = "product_bundle_mapping",
//		joinColumns = @JoinColumn(name = "bundle_id"),
//		inverseJoinColumns = @JoinColumn(name = "product_id"))
//	private Set<ProductEntity> bundleItems = new LinkedHashSet<>();

//	@Builder.Default
//	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//	@JoinTable(name = "product_bundle_mapping",
//		joinColumns = @JoinColumn(name = "product_id"),
//		inverseJoinColumns = @JoinColumn(name = "bundle_id"))
//	private Set<ProductEntity> bundles = new LinkedHashSet<>();
}
