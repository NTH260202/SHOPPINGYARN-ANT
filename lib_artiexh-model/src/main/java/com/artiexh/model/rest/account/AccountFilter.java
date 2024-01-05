package com.artiexh.model.rest.account;

import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.model.domain.Role;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountFilter {
	private Set<Role> roles;
	private String username;

	public Specification<AccountEntity> getSpecification() {
		return (root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (roles != null && !roles.isEmpty()) {
				predicates.add(root.get("role").in(roles.stream().map(Role::getByteValue).collect(Collectors.toSet())));
			}

			if (StringUtils.isNotBlank(username)) {
				predicates.add(builder.like(root.get("username"), "%" + username + "%"));
			}
			return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
