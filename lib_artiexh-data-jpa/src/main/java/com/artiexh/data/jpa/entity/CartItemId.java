package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartItemId implements Serializable {
	@Serial
	private static final long serialVersionUID = -8902113740013463137L;

	@Column(name = "cart_id", nullable = false)
	private Long cartId;

	@Column(name = "campaign_sale_id", nullable = false)
	private Long campaignSaleId;

	@Column(name = "product_code", nullable = false, length = 20)
	private String productCode;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		CartItemId entity = (CartItemId) o;
		return Objects.equals(this.campaignSaleId, entity.campaignSaleId) &&
			Objects.equals(this.productCode, entity.productCode) &&
			Objects.equals(this.cartId, entity.cartId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(campaignSaleId, productCode, cartId);
	}

}