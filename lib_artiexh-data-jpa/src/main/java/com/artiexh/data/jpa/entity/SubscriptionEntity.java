package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
	@EmbeddedId
	private SubscriptionEntityId id;

	@MapsId("artistId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "artist_id", nullable = false)
	private ArtistEntity artist;

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "expired_datetime", nullable = false)
	private Instant expiredDatetime;

}