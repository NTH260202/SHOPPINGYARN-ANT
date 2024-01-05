package com.artiexh.api.service.campaign;

import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import com.artiexh.model.domain.ProductInCampaign;

public interface ProductInCampaignService {
	ProductInCampaign update(ProductInCampaignEntity productInCampaign, ProductInCampaign product);
}
