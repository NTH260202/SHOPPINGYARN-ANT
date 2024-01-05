package com.artiexh.model.rest.campaign.response;

import com.artiexh.model.domain.CampaignHistory;
import com.artiexh.model.rest.provider.ProviderInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CampaignDetailResponse extends CampaignResponse {
	private ProviderInfo provider;
	private Set<ProductInCampaignResponse> products;
	private Set<CampaignHistory> campaignHistories;
	private String content;
	private Integer adminProfitPercentage;
}
