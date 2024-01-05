package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CampaignHistoryId implements Serializable {
	@Serial
	private static final long serialVersionUID = -2185902338952211019L;

	@Column(name = "campaign_id", nullable = false)
	private Long campaignId;

	@Column(name = "event_time", nullable = false)
	@CreatedDate
	private Instant eventTime;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		CampaignHistoryId entity = (CampaignHistoryId) o;
		return Objects.equals(this.campaignId, entity.campaignId) &&
			Objects.equals(this.eventTime, entity.eventTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(campaignId, eventTime);
	}

}