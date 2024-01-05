package com.artiexh.data.jpa.entity.embededmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
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
public class ProductVariantProviderId implements Serializable {
	@Serial
	private static final long serialVersionUID = 730123199711833123L;

	@NotNull
	@Column(name = "variant_id", nullable = false)
	private Long productVariantId;

	@NotNull
	@Column(name = "business_code", nullable = false, length = 13)
	private String businessCode;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ProductVariantProviderId entity = (ProductVariantProviderId) o;
		return Objects.equals(this.productVariantId, entity.productVariantId) &&
			Objects.equals(this.businessCode, entity.businessCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productVariantId, businessCode);
	}
}
