package com.artiexh.model.rest.producthistory.producthistory;

import com.artiexh.data.jpa.entity.ProductHistoryEntity;
import com.artiexh.model.domain.ProductHistoryAction;
import com.artiexh.model.domain.SourceCategory;
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
public class ProductHistoryFilter {
	private ProductHistoryAction action;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceId;
	private SourceCategory sourceCategory;

	public Specification<ProductHistoryEntity> getSpecification() {
		List<Predicate> predicates = new ArrayList<>();
		return (root, cQuery, builder) -> {
			if (action != null) {
				predicates.add(builder.equal(root.get("action"), action));
			}
			if (sourceId != null) {
				predicates.add(builder.equal(root.get("sourceId"), sourceId));
			}
			if (sourceCategory != null) {
				predicates.add(builder.equal(root.get("sourceCategory"), sourceCategory.getByteValue()));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
