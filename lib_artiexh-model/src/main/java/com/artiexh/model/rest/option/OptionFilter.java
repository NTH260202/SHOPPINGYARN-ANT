package com.artiexh.model.rest.option;

import com.artiexh.data.jpa.entity.ProductOptionEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionFilter {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productTemplateId;

	public Specification<ProductOptionEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (productTemplateId != null) {
				predicates.add(builder.equal(root.get("productTemplateId"), productTemplateId));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
