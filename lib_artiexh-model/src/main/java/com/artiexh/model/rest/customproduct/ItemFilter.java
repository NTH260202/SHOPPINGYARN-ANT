package com.artiexh.model.rest.customproduct;

import com.artiexh.data.jpa.entity.CustomProductEntity;
import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import com.artiexh.data.jpa.entity.ProductVariantEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFilter {
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonIgnore
	private Long artistId;
	private String name;
	private Set<Long> categoryIds;
	private Set<String> providerIds;

	public Specification<CustomProductEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("artist").get("id"), artistId));

			predicates.add(builder.equal(root.get("isDeleted"), false));

			if (name != null) {
				predicates.add(builder.like(root.get("name"), "%" + name + "%"));
			}

			if (categoryIds != null && !categoryIds.isEmpty()) {
				Join<CustomProductEntity, ProductTemplateEntity> productTemplateJoin = root.join("variant").join("productTemplate");
				predicates.add(productTemplateJoin.get("category").get("id").in(categoryIds));
			}

			if (providerIds != null && !providerIds.isEmpty()) {
				Join<CustomProductEntity, ProductVariantEntity> productVariantJoin = root.join("variant");
				predicates.add(productVariantJoin.get("providerConfigs").get("provider").get("id").in(providerIds));
			}

			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
