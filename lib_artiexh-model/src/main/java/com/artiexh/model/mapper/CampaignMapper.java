package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CampaignEntity;
import com.artiexh.data.jpa.entity.CampaignHistoryEntity;
import com.artiexh.model.domain.*;
import com.artiexh.model.rest.campaign.response.CampaignDetailResponse;
import com.artiexh.model.rest.campaign.response.CampaignResponse;
import com.artiexh.model.rest.campaign.response.PublishedCampaignDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductInCampaignMapper.class, ProviderMapper.class, CampaignTypeMapper.class}
)
public interface CampaignMapper {

	@Named("entityToResponse")
	CampaignResponse entityToResponse(CampaignEntity entity);

	CampaignResponse domainToResponse(Campaign domain);

	@Mapping(target = "campaignHistories", ignore = true)
	Campaign entityToDomain(CampaignEntity entity);

	@Named("entityToDetailResponse")
	@Mapping(target = "products", source = "productInCampaigns")
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "campaignHistories", ignore = true)
	@Mapping(target = "campaignSaleId", source = "campaignSale.id")
	CampaignDetailResponse entityToDetailResponse(CampaignEntity entity);

	@Mapping(target = "provider", ignore = true)
	PublishedCampaignDetailResponse entityToPublishedCampaignDetailResponse(CampaignEntity entity);

	@Mapping(target = "eventTime", source = "id.eventTime")
	@Mapping(target = "campaignId", source = "id.campaignId")
	CampaignHistory campaignHistoryEntityToCampaignHistory(CampaignHistoryEntity entity);

	ProductSource entityToSourceDomain(CampaignEntity campaign);

	default CampaignStatus campaignStatusFrom(byte value) {
		return CampaignStatus.fromValue(value);
	}

	default byte campaignStatusTo(CampaignStatus status) {
		return (byte) status.getValue();
	}

	default CampaignHistoryAction campaignHistoryActionFrom(byte value) {
		return CampaignHistoryAction.fromValue(value);
	}

	default byte campaignHistoryActionTo(CampaignHistoryAction status) {
		return (byte) status.getValue();
	}
}
