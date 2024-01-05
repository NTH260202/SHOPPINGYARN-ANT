package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductVariantProviderEntity;
import com.artiexh.data.jpa.entity.ProviderEntity;
import com.artiexh.model.domain.Provider;
import com.artiexh.model.rest.campaign.response.CampaignProviderResponse;
import com.artiexh.model.rest.campaign.response.ProviderConfigResponse;
import com.artiexh.model.rest.provider.ProviderDetail;
import com.artiexh.model.rest.provider.ProviderInfo;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductVariantMapper.class, ProductTemplateMapper.class, ProviderCategoryMapper.class}
)
public interface ProviderMapper {
	//Provider entityToDomain(ProviderEntity entity);

	@Mapping(target = "categories", source = "categoryIds")
	Provider detailToDomain(ProviderDetail detail);

	ProviderEntity domainToEntity(Provider domain);

	@Mapping(target = "productTemplates", ignore = true)
	ProviderEntity domainToEntity(Provider domain, @MappingTarget ProviderEntity entity);

	@Mapping(target = "isDeleted", source = "deleted")
	Provider entityToDomain(ProviderEntity entity, @Context CycleAvoidingMappingContext context);

	@Named("entityToBasicDomain")
	@Mapping(target = "productVariants", ignore = true)
	@Mapping(target = "productTemplates", ignore = true)
	@Mapping(target = "isDeleted", source = "deleted")
	Provider entityToBasicDomain(ProviderEntity entity);

	@Mapping(target = "productTemplates", source = "productTemplates", qualifiedByName = "domainSetToInfoSet")
	@Mapping(target = "isDeleted", source = "deleted")
	ProviderDetail domainToDetail(Provider domain);

	@Mapping(target = "productTemplates", source = "productTemplates", ignore = true)
	@Named("domainToDetailWithoutProductTemplates")
	@Mapping(target = "isDeleted", source = "deleted")
	ProviderDetail domainToDetailWithoutProductTemplates(Provider domain);

	@IterableMapping(qualifiedByName = "domainToDetailWithoutProductTemplates")
	@Named("domainSetToDetailSetWithoutProductTemplates")
	Set<ProviderDetail> domainSetToDetailSetWithoutProductTemplates(Set<Provider> domain);

	@Named("domainToInfo")
	@Mapping(target = "isDeleted", source = "deleted")
	ProviderInfo domainToInfo(Provider domain);

	@Mapping(target = "categoryIds", ignore = true)
	ProviderInfo entityToInfo(ProviderEntity domain);

	default Provider businessCodeToDomain(String businessCode) {
		return Provider.builder().businessCode(businessCode).build();
	}

	@Mapping(target = "customProducts", ignore = true)
	CampaignProviderResponse entityToCampaignProviderResponse(ProviderEntity entity);

	ProviderConfigResponse entityToCampaignProviderConfig(ProductVariantProviderEntity entity);
}
