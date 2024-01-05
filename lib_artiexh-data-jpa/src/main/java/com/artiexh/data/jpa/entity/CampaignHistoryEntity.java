package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@Entity
@Table(name = "campaign_history")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CampaignHistoryEntity {
	@EmbeddedId
	private CampaignHistoryId id;

	@Column(name = "action", nullable = false)
	private Byte action;

	@Column(name = "message", length = 1000)
	private String message;

	@Builder.Default
	@Column(name = "updated_by", nullable = false)
	private String updatedBy = "";

}