package com.artiexh.model.rest.order.request;

import com.artiexh.data.jpa.entity.OrderEntity;
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
public class OrderPageFilter {
	private Instant from;
	private Instant to;
	private String ownerUsername;

	public Specification<OrderEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (from != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("createdDate"), from));
			}
			if (to != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("createdDate"), to));
			}
			if (StringUtils.isNotBlank(ownerUsername)) {
				predicates.add(builder.like(root.get("user").get("username"), "%" + ownerUsername + "%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
