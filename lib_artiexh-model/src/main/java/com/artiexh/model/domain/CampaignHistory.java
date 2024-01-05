package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignHistory {
	@JsonIgnore
	private Long campaignId;
	private Instant eventTime;
	private CampaignHistoryAction action;
	private String message;
	private String updatedBy;
}
