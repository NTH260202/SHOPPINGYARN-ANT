package com.artiexh.model.rest.campaign.request;

import com.artiexh.data.jpa.entity.CampaignEntity;
import com.artiexh.model.domain.CampaignType;
import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class PublicCampaignFilter {
	public Specification<CampaignEntity> getSpecification() {
		return ((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			predicates.add(builder.equal(root.get("type"), CampaignType.PUBLIC.getByteValue()));

			return builder.and(predicates.toArray(new Predicate[0]));
		});
	}
}
