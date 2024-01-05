package com.artiexh.model.rest.marketplace.salecampaign.request;

import com.artiexh.model.domain.CampaignSaleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaleCampaignStatusRequest {
	private CampaignSaleStatus status;
}
