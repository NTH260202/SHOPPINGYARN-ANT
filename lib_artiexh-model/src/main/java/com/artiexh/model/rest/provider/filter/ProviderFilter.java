package com.artiexh.model.rest.provider.filter;

import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import com.artiexh.data.jpa.entity.ProviderCategoryEntity;
import com.artiexh.data.jpa.entity.ProviderEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderFilter {
	@JsonSerialize(using = StringArraySerializer.class)
	private Long[] productTemplateIds;
	private String businessName;
	@JsonSerialize(using = ToStringSerializer.class)
	private Set<Long> categoryIds;
	private Boolean isDeleted;

	public Specification<ProviderEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (isDeleted != null) {
				predicates.add(builder.equal(root.get("isDeleted"), isDeleted));
			}
			if (StringUtils.isNotEmpty(businessName)) {
				predicates.add(builder.like(root.get("businessName"), "%" + businessName + "%"));
			}
			if (productTemplateIds != null && productTemplateIds.length > 0) {
				Join<ProviderEntity, ProductTemplateEntity> joinProviderProvidedProduct = root.join("productTemplates");
				predicates.add(joinProviderProvidedProduct.get("id").in(Arrays.asList(productTemplateIds)));
			}
			if (categoryIds != null) {
				Join<ProviderEntity, ProviderCategoryEntity> joinProviderCategory = root.join("categories");
				predicates.add(joinProviderCategory.get("id").in(categoryIds));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
