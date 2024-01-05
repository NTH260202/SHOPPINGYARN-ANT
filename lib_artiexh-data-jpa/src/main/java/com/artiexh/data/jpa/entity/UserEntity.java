package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends AccountEntity {

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private final Set<SubscriptionEntity> subscriptionsTo = new LinkedHashSet<>();

	@Column(name = "facebook_id", length = 20)
	private String facebookId;

	@Column(name = "google_id", length = 21)
	private String googleId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private CartEntity shoppingCart;

	@Builder.Default
	@OneToMany(mappedBy = "user")
	private Set<UserAddressEntity> addresses = new LinkedHashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "user")
	private Set<OrderEntity> orderGroups = new LinkedHashSet<>();

}