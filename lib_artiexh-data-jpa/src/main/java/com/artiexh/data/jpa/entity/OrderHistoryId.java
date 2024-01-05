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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderHistoryId implements Serializable {
	@Serial
	private static final long serialVersionUID = 6651597644338920273L;

	@Column(name = "campaign_order_id", nullable = false)
	private Long campaignOrderId;

	@Column(name = "status", nullable = false)
	private Byte status;

    @Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		OrderHistoryId entity = (OrderHistoryId) o;
		return Objects.equals(this.campaignOrderId, entity.campaignOrderId) &&
			Objects.equals(this.status, entity.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(campaignOrderId, status);
	}

}