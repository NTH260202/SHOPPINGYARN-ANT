package com.artiexh.model.rest.provider.filter;

import com.artiexh.data.jpa.entity.CollectionEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
import jakarta.persistence.criteria.Predicate;
import lombok.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionFilter {
	@JsonSerialize(using = StringArraySerializer.class)
	private Long artistId;

	public Specification<CollectionEntity> getSpecification() {
		return (root, cQuery, builder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (artistId != null) {
				predicates.add(builder.equal(root.get("artistId"), artistId));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
