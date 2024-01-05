package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campaign")
public class CampaignEntity {

	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "status", nullable = false)
	private Byte status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	private ArtistEntity owner;

	@Builder.Default
	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductInCampaignEntity> productInCampaigns = new LinkedHashSet<>();

	@Column(name = "provider_id", length = 13)
	private String providerId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", length = 1000)
	private String description;

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	@JoinColumn(name = "campaign_id")
	@OrderBy("id.eventTime desc")
	private Set<CampaignHistoryEntity> campaignHistories = new LinkedHashSet<>();

	@Column(name = "thumbnail_url", length = 2048)
	private String thumbnailUrl;

	@Column(name = "content", columnDefinition = "text /*!100301 COMPRESSED*/(0, 0)")
	private String content;

	@Builder.Default
	@Column(name = "is_finalized", nullable = false)
	private Boolean isFinalized = false;

	@Column(name = "type", nullable = false)
	private Byte type;

	@Column(name = "`from`")
	private Instant from;

	@Column(name = "`to`")
	private Instant to;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "created_by", nullable = false)
	private AccountEntity createdBy;

	@OneToOne(mappedBy = "campaignRequest")
	private CampaignSaleEntity campaignSale;

	@Column(name = "admin_profit_percentage")
	private Integer adminProfitPercentage;

}