package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {

	@Id
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "cart_id")
	private Set<CartItemEntity> cartItems = new LinkedHashSet<>();

}