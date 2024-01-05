package com.artiexh.data.jpa.entity.embededmodel;

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
@Embeddable
@Builder(toBuilder = true)
public class ProductHistoryEntityDetailId implements Serializable {
	@Serial
	private static final long serialVersionUID = 850123199711833123L;
	@Column(name = "product_code", nullable = false)
	private String productCode;

	@Column(name = "product_history_id", nullable = false)
	private Long productHistoryId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ProductHistoryEntityDetailId entity = (ProductHistoryEntityDetailId) o;
		return Objects.equals(this.productCode, entity.productCode) &&
			Objects.equals(this.productHistoryId, entity.productHistoryId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productCode, productHistoryId);
	}
}
