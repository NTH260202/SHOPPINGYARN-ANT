package com.artiexh.model.rest.shop;

import com.artiexh.data.jpa.entity.ArtistEntity;
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
public class ShopPageFilter {
	private String shopName;
	private Long ownerId;
	private String ownerUserName;

	public Specification<ArtistEntity> getSpecification() {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (shopName != null) {
				predicates.add(builder.like(root.get("shopName"), '%' + shopName + '%'));
			}
			if (ownerId != null) {
				predicates.add(builder.equal(root.get("id"), ownerId));
			}
			if (ownerUserName != null) {
				predicates.add(builder.equal(root.get("username"), ownerUserName));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
