package com.artiexh.model.rest.producttemplate;

import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTemplateFilter {
	private String name;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long categoryId;
	private Boolean isDeleted;

	public Specification<ProductTemplateEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(root.get("hasVariant"), true));
			if (isDeleted != null) {
				predicates.add(builder.equal(root.get("isDeleted"), isDeleted));
			}
			if (StringUtils.isNotBlank(name)) {
				predicates.add(builder.like(root.get("name"), "%" + name + "%"));
			}
			if (categoryId != null) {
				predicates.add(builder.equal(root.get("category").get("id"), categoryId));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
