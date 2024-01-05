package com.artiexh.model.rest.productinventory;

import com.artiexh.data.jpa.entity.ProductInventoryEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventoryFilter {
	private Long ownerId;
	private String productCode;
	private Set<String> productCodes;

	public Specification<ProductInventoryEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("isDeleted"), false));
			if (ownerId != null) {
				predicates.add(builder.equal(root.get("owner").get("id"), ownerId));
			}

			if (StringUtils.isNotBlank(productCode)) {
				predicates.add(builder.like(root.get("productCode"), "%" + productCode + "%"));
			}

			if (productCodes != null && !productCodes.isEmpty()) {
				predicates.add(root.get("productCode").in(productCodes));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
