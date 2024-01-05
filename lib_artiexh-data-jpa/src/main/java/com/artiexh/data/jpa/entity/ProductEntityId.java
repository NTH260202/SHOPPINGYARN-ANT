package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ProductEntityId implements Serializable {
	@Serial
	private static final long serialVersionUID = 762299777384907757L;

	@Column(name = "product_code", nullable = false, length = 20)
	private String productCode;

	@Column(name = "campaign_sale_id", nullable = false)
	private Long campaignSaleId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ProductEntityId entity = (ProductEntityId) o;
		return Objects.equals(this.campaignSaleId, entity.campaignSaleId) &&
			Objects.equals(this.productCode, entity.productCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(campaignSaleId, productCode);
	}

}