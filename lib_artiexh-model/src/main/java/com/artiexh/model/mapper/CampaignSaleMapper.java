package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CampaignSaleEntity;
import com.artiexh.data.opensearch.model.ProductDocument;
import com.artiexh.model.domain.CampaignSaleStatus;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignDetailResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductMapper.class, CampaignTypeMapper.class}
)
public interface CampaignSaleMapper {

	@Named("entityToResponse")
	SaleCampaignResponse entityToResponse(CampaignSaleEntity entity);

	@Mapping(target = "campaignRequestId", source = "campaignRequest.id")
	@Named("entityToDetailResponse")
	SaleCampaignDetailResponse entityToDetailResponse(CampaignSaleEntity entity);

	ProductDocument.Campaign entityToDocument(CampaignSaleEntity entity);

	default CampaignSaleStatus campaignSaleStatusFrom(Integer status) {
		return CampaignSaleStatus.from(status);
	}

	default CampaignSaleStatus campaignSaleStatusFrom(Byte status) {
		return CampaignSaleStatus.from(status);
	}

	default byte campaignSaleStatusToByte(CampaignSaleStatus status) {
		return status.getByteValue();
	}

}
