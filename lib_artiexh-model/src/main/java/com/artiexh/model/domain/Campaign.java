package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

	private Long id;

	private CampaignStatus status;

	private String name;

	private String description;

	private Boolean isPrePublished;

	private String thumbnailUrl;

	private String content;

	private CampaignType type;

	private Instant from;

	private Instant to;

	private Set<CampaignHistory> campaignHistories;

}
