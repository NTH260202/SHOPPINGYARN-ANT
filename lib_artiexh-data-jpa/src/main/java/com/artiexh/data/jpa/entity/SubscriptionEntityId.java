package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class SubscriptionEntityId implements Serializable {

	@Serial
	private static final long serialVersionUID = -4029131288047614739L;

	@Column(name = "artist_id", nullable = false)
	private Long artistId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		SubscriptionEntityId entity = (SubscriptionEntityId) o;
		return Objects.equals(this.artistId, entity.artistId) &&
			Objects.equals(this.userId, entity.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(artistId, userId);
	}

}