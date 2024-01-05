package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductVariantCombinationEntity;
import com.artiexh.data.jpa.entity.ProductVariantEntity;
import com.artiexh.data.jpa.entity.ProductVariantProviderEntity;
import com.artiexh.data.jpa.entity.ProviderEntity;
import com.artiexh.model.domain.ProductVariant;
import com.artiexh.model.domain.ProductVariantProvider;
import com.artiexh.model.domain.ProvidedProductType;
import com.artiexh.model.domain.VariantCombination;
import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import com.artiexh.model.rest.productvariant.request.UpdateProductVariantDetail;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductTemplateMapper.class, ProviderMapper.class}
)
public interface ProductVariantMapper {
	ProductVariant entityToDomain(ProductVariantEntity entity, @Context CycleAvoidingMappingContext context);

	@Named("entityToBasicDomain")
	@Mapping(target = "productTemplate", qualifiedByName = "entityToBasicDomain")
	@Mapping(target = "providerConfigs", qualifiedByName = "entitySetToBasicDomainSet")
	ProductVariant entityToBasicDomain(ProductVariantEntity entity);

	@Mapping(target = "productTemplate", ignore = true)
	@Named("detailToDomain")
	ProductVariant detailToDomain(ProductVariantDetail detail);

	@IterableMapping(qualifiedByName = "detailToDomain")
	Set<ProductVariant> detailSetToDomainSet(Set<ProductVariantDetail> detail);

	ProductVariant updateRequestToDomain(UpdateProductVariantDetail detail);

	@Mapping(target = "productTemplate", source = "productTemplate", qualifiedByName = "domainToInfo")
	@Named("domainToDetail")
	ProductVariantDetail domainToDetail(ProductVariant domain);

	@IterableMapping(qualifiedByName = "domainToDetail")
	Set<ProductVariantDetail> domainSetToDetailSet(Set<ProductVariant> domain);

	@Mapping(target = "providerConfigs", ignore = true)
	@Mapping(target = "variantCombinations", ignore = true)
	ProductVariantEntity domainToEntity(ProductVariant domain);

	@Mapping(target = "productTemplate", ignore = true)
	@Mapping(target = "productTemplateId", ignore = true)
	@Mapping(target = "variantCombinations", ignore = true)
	@Mapping(target = "providerConfigs", ignore = true)
	ProductVariantEntity domainToEntity(ProductVariant domain, @MappingTarget ProductVariantEntity entity);

	@Mapping(source = "id.variantId", target = "variantId")
	@Mapping(source = "id.optionValueId", target = "optionValueId")
	VariantCombination entityToDomain(ProductVariantCombinationEntity entity);

	@Mapping(target = "id.variantId", source = "variantId")
	@Mapping(target = "id.optionValueId", source = "optionValueId")
	ProductVariantCombinationEntity domainToEntity(VariantCombination domain);

	@Mapping(source = "id.productVariantId", target = "variantId")
	@Mapping(source = "id.businessCode", target = "businessCode")
	ProductVariantProvider entityToDomain(ProductVariantProviderEntity productVariantProviderEntity, @Context CycleAvoidingMappingContext context);

	@Named("entityToBasicDomain")
	@Mapping(source = "id.productVariantId", target = "variantId")
	@Mapping(source = "id.businessCode", target = "businessCode")
	@Mapping(target = "provider", qualifiedByName = "entityToBasicDomain")
	ProductVariantProvider entityToBasicDomain(ProductVariantProviderEntity productVariantProviderEntity);

	@IterableMapping(qualifiedByName = "entityToBasicDomain")
	@Named("entitySetToBasicDomainSet")
	Set<ProductVariantProvider> entitySetToBasicDomainSet(Set<ProductVariantProviderEntity> entity);

	@Mapping(target = "id.productVariantId", source = "variantId")
	@Mapping(target = "id.businessCode", source = "businessCode")
	@Mapping(target = "provider", source = "businessCode", qualifiedByName = "idToEntity")
	ProductVariantProviderEntity domainToEntity(ProductVariantProvider entity);

	@Mapping(target = "provider", qualifiedByName = "domainToInfo")
	ProductVariantDetail.ProviderConfig domainToDetail(ProductVariantProvider domain);

	@Named("idToDomain")
	default ProductVariant idToDomain(Long variantId) {
		return ProductVariant.builder().id(variantId).build();
	}

	@Named("idToEntity")
	default ProviderEntity idToDomain(String businessCode) {
		return ProviderEntity.builder().businessCode(businessCode).build();
	}

	default Integer toValue(ProvidedProductType type) {
		return type.getValue();
	}

	default ProvidedProductType toProvidedProductType(Integer value) {
		return ProvidedProductType.fromValue(value);
	}
}
