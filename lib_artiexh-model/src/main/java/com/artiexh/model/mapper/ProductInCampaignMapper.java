package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import com.artiexh.model.domain.ProductInCampaign;
import com.artiexh.model.rest.campaign.request.FinalizeProductRequest;
import com.artiexh.model.rest.campaign.request.ProductInCampaignRequest;
import com.artiexh.model.rest.campaign.response.ProductInCampaignDetailResponse;
import com.artiexh.model.rest.campaign.response.ProductInCampaignResponse;
import org.mapstruct.*;

import java.util.Set;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductAttachMapper.class, CustomProductMapper.class, ArtistMapper.class, CampaignMapper.class, DateTimeMapper.class, CampaignTypeMapper.class})
public interface ProductInCampaignMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "campaign", ignore = true)
	@Mapping(target = "priceUnit", source = "price.unit")
	@Mapping(target = "priceAmount", source = "price.amount")
	@Mapping(target = "customProduct.id", source = "customProductId")
	ProductInCampaignEntity createRequestToEntity(ProductInCampaignRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "campaign", ignore = true)
	@Mapping(target = "priceUnit", source = "price.unit")
	@Mapping(target = "priceAmount", source = "price.amount")
	@Mapping(target = "customProduct.id", source = "customProductId")
	void createRequestToEntity(ProductInCampaignRequest request, @MappingTarget ProductInCampaignEntity entity);

	@Named("entityToResponse")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "customProduct", source = ".", qualifiedByName = "productInCampaignEntityToGeneralResponse")
	@Mapping(target = "providerConfig.basePriceAmount", source = "basePriceAmount")
	@Mapping(target = "providerConfig.manufacturingTime", source = "manufacturingTime")
	@Mapping(target = "providerConfig.minQuantity", source = "minQuantity")
	ProductInCampaignResponse entityToResponse(ProductInCampaignEntity product);

	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "customProduct", source = ".", qualifiedByName = "productInCampaignEntityToDetailResponse")
	@Mapping(target = "providerConfig", ignore = true)
	ProductInCampaignDetailResponse entityToDetailResponse(ProductInCampaignEntity product);

	@IterableMapping(qualifiedByName = "entityToResponse")
	Set<ProductInCampaignResponse> entityToResponse(Set<ProductInCampaignEntity> product);

	@Named("entityToDomain")
	@Mapping(target = "price.amount", source = "priceAmount")
	@Mapping(target = "price.unit", source = "priceUnit")
	@Mapping(target = "customProduct", qualifiedByName = "entityToDomainWithoutVariant")
	ProductInCampaign entityToDomain(ProductInCampaignEntity product);

	@Named("domainToEntity")
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	@Mapping(target = "priceUnit", source = "price.unit")
	@Mapping(target = "priceAmount", source = "price.amount")
	ProductInCampaignEntity domainToEntity(ProductInCampaign productInCampaign);

	@Mapping(target = "campaign", ignore = true)
	ProductInCampaign requestToDomain(FinalizeProductRequest product);
}
