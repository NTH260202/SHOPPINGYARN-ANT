package com.artiexh.model.rest.marketplace.salecampaign.filter;

import com.artiexh.data.jpa.entity.CampaignSaleEntity;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.domain.CampaignType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleCampaignFilter {
	private Long ownerId;
	private String username;
	private CampaignType campaignType;
	private CampaignSaleStatus status;
	private Instant from;
	private Instant to;
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

			if (campaignType != null) {
				predicates.add(builder.equal(root.get("type"), campaignType.getByteValue()));
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
}
