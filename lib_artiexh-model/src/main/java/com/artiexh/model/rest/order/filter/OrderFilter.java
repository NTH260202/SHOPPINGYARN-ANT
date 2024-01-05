package com.artiexh.model.rest.order.filter;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.data.jpa.entity.OrderEntity;
import com.artiexh.model.domain.CampaignOrderStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilter {

	private CampaignOrderStatus status;
	private Instant from;
	private Instant to;

	public Specification<OrderEntity> getSpecificationForUser(Long userId) {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("user").get("id"), userId));
			if (status != null) {
				Join<CampaignOrderEntity, OrderEntity> orderJoin = root.join("campaignOrders");
				predicates.add(builder.equal(orderJoin.get("status"), status.getByteValue()));
			}
			if (from != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
			}
			if (to != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), from));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
