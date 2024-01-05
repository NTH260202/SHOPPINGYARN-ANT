package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "custom_product")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class CustomProductEntity extends BaseAuditEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	@OneToMany(orphanRemoval = true, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "custom_product_id")
	private Set<ImageSetEntity> imageSet;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "artist_id", nullable = false)
	private ArtistEntity artist;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REMOVE})
	@JoinColumn(name = "variant_id", nullable = false)
	private ProductVariantEntity variant;

	@Column(name = "combination_code", nullable = false)
	private String combinationCode;

	@Column(name = "campaign_lock")
	private Long campaignLock;

	@Column(name = "description", length = 1000)
	private String description;

	@Builder.Default
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "custom_product_id", updatable = false)
	private Set<CustomProductTagEntity> tags = new LinkedHashSet<>();

	@OneToOne
	@JoinColumn(name = "model_thumbnail_id")
	private MediaEntity modelThumbnail;

	@ManyToOne()
	@JoinColumn(name = "category_id")
	private ProductCategoryEntity category;

	@Column(name = "max_item_per_order")
	private Integer maxItemPerOrder;

	@OneToMany(
		fetch = FetchType.LAZY,
		orphanRemoval = true,
		cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "custom_product_id")
	private Set<ProductAttachEntity> attaches;

	@Builder.Default
	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

}
