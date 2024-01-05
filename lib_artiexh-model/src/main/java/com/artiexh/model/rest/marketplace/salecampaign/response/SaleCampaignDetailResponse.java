package com.artiexh.model.rest.marketplace.salecampaign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleCampaignDetailResponse extends SaleCampaignResponse {
	private String content;
}
