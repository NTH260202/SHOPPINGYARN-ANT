package com.artiexh.model.rest.provider.filter;

import com.artiexh.data.jpa.entity.ProductVariantProviderEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderConfigFilter {
	private Long variantId;
	@JsonIgnore
	private String businessCode;

	public Specification<ProductVariantProviderEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (businessCode != null) {
				predicates.add(builder.equal(root.get("id").get("businessCode"), businessCode));
			}
			if (variantId != null) {
				predicates.add(builder.equal(root.get("id").get("productVariantId"), variantId));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
