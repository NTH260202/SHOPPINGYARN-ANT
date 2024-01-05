package com.artiexh.model.rest.campaign.response;

import com.artiexh.model.rest.provider.ProviderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PublishedCampaignDetailResponse extends CampaignResponse {
	private ProviderInfo provider;
	private String content;
}
