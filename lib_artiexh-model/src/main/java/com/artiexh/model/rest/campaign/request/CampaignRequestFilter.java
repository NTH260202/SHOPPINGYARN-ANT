package com.artiexh.model.rest.campaign.request;

import com.artiexh.data.jpa.entity.CampaignEntity;
import com.artiexh.model.domain.CampaignStatus;
import com.artiexh.model.domain.CampaignType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequestFilter {
	private Set<CampaignStatus> status;
	private String ownerId;
	private String providerId;
	private CampaignType campaignType;
	private Instant from = Instant.now();
	private Instant to;

	public Specification<CampaignEntity> getSpecification() {
		return ((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (status != null && !status.isEmpty()) {
				predicates.add(root.get("status").in(
					status.stream().map(CampaignStatus::getByteValue).collect(Collectors.toSet()))
				);
			}

			if (ownerId != null) {
				predicates.add(builder.equal(root.get("owner").get("id"), ownerId));
			}

			if (providerId != null) {
				predicates.add(builder.equal(root.get("providerId"), providerId));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		});
	}

	private Specification<CampaignEntity> getProgressPublishedCampaign() {
		return ((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.or(builder.equal(root.get("isPrePublished"), true)));

			if (campaignType != null && CampaignType.MARKETPLACE_VIEW_TYPE.contains(campaignType)) {
				predicates.add(builder.equal(root.get("type"), campaignType.getByteValue()));
			} else {
				predicates.add(root.get("type").in(
					CampaignType.MARKETPLACE_VIEW_TYPE.stream()
						.map(CampaignType::getByteValue)
						.collect(Collectors.toSet())
				));
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
		});
	}

	public Specification<CampaignEntity> getMarketPlaceSpecification() {
		return Specification.where(getSpecification())
			.and(getProgressPublishedCampaign());
	}
}
