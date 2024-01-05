package com.artiexh.model.rest.marketplace.salecampaign.filter;

import com.artiexh.data.jpa.entity.CampaignSaleEntity;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.domain.CampaignType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketplaceSaleCampaignFilter {
	private Long ownerId;
	private String username;
	private CampaignType campaignType;
	private Instant from;
	private Instant to;
	private CampaignSaleStatus status;
	private String name;

	public Specification<CampaignSaleEntity> getSpecification() {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.isNotBlank(name)) {
				predicates.add(builder.like(root.get("name"), "%" + name + "%"));
			}

			if (status != null) {
				predicates.add(builder.equal(root.get("status"), status.getByteValue()));
			}

			if (ownerId != null) {
				predicates.add(builder.equal(root.get("owner").get("id"), ownerId));
			}

			if (username != null) {
				predicates.add(builder.equal(root.get("owner").get("username"), username));
			}

			if (to != null) {
				List<Predicate> toPredicate = new ArrayList<>();
				toPredicate.add(builder.lessThanOrEqualTo(root.get("from"), to));
				toPredicate.add(builder.lessThanOrEqualTo(root.get("to"), to));
				predicates.add(builder.or(toPredicate.toArray(new Predicate[0])));
			}

			if (from != null) {
				List<Predicate> fromPredicate = new ArrayList<>();
				fromPredicate.add(builder.greaterThanOrEqualTo(root.get("from"), from));
				fromPredicate.add(builder.greaterThanOrEqualTo(root.get("to"), from));
				predicates.add(builder.or(fromPredicate.toArray(new Predicate[0])));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

	public Specification<CampaignSaleEntity> getMarketplaceSpecification(boolean isArtistPage) {
		Specification<CampaignSaleEntity> marketplaceCampaignSpec = (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			List<Predicate> activeCampaignPredicate = new ArrayList<>();
			activeCampaignPredicate.add(builder.lessThanOrEqualTo(root.get("publicDate"), Instant.now()));
			activeCampaignPredicate.add(builder.lessThanOrEqualTo(root.get("from"), Instant.now()));

			predicates.add(builder.or(activeCampaignPredicate.toArray(new Predicate[0])));

			predicates.add(builder.equal(root.get("status"), CampaignSaleStatus.ACTIVE.getByteValue()));

			Set<CampaignType> allowedViewType = isArtistPage ? CampaignType.ARTIST_VIEW_TYPE : CampaignType.MARKETPLACE_VIEW_TYPE;

			if (campaignType != null && allowedViewType.contains(campaignType)) {
				predicates.add(builder.equal(root.get("type"), campaignType.getByteValue()));
			} else {
				predicates.add(root.get("type").in(
					allowedViewType.stream()
						.map(CampaignType::getByteValue)
						.collect(Collectors.toSet())
				));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
		return getSpecification().and(marketplaceCampaignSpec);
	}

}
