package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItemEntity {
	@EmbeddedId
	private CartItemId id;

	@MapsId("id")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_sale_id", referencedColumnName = "campaign_sale_id", nullable = false)
	@JoinColumn(name = "product_code", referencedColumnName = "product_code", nullable = false)
	private ProductEntity product;

	@Builder.Default
	@Column(name = "quantity", nullable = false)
	private Integer quantity = 0;

}