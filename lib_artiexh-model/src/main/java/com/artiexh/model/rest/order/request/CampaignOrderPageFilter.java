package com.artiexh.model.rest.order.request;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.model.domain.CampaignOrderStatus;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignOrderPageFilter {
	private CampaignOrderStatus status;
	private Instant from;
	private Instant to;

	public Specification<CampaignOrderEntity> getSpecificationForArtist(Long shopId) {
		return getSpecification(root -> root.get("campaignSale").get("owner").get("id"), shopId);
	}

	public Specification<CampaignOrderEntity> getSpecificationForUser(Long userId) {
		return getSpecification(root -> root.get("order").get("user").get("id"), userId);
	}

	private Specification<CampaignOrderEntity> getSpecification(Function<Root<CampaignOrderEntity>, Path<Long>> identifier,
																Long id) {
		return getSpecification()
			.and((root, query, builder) -> builder.equal(identifier.apply(root), id));
	}

	public Specification<CampaignOrderEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (status != null) {
				predicates.add(builder.equal(root.get("status"), status.getByteValue()));
			}
			if (from != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
			}
			if (to != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}


}
