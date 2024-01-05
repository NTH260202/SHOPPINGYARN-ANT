package com.artiexh.api.service.campaign.impl;

import com.artiexh.api.service.campaign.ProductInCampaignService;
import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import com.artiexh.data.jpa.repository.ProductInCampaignRepository;
import com.artiexh.model.domain.ProductInCampaign;
import com.artiexh.model.mapper.ProductInCampaignMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInCampaignServiceImpl implements ProductInCampaignService {
	private final ProductInCampaignRepository productInCampaignRepository;
	private final ProductInCampaignMapper productInCampaignMapper;

	@Override
	@Transactional
	public ProductInCampaign update(ProductInCampaignEntity productInCampaign, ProductInCampaign product) {

		productInCampaign.setPriceAmount(product.getPrice().getAmount());

		productInCampaignRepository.save(productInCampaign);

		productInCampaignMapper.entityToDomain(productInCampaign);
		return productInCampaignMapper.entityToDomain(productInCampaign);
	}
}
