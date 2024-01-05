package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import com.artiexh.model.domain.*;
import com.artiexh.model.rest.producttemplate.ProductTemplateDetail;
import com.artiexh.model.rest.producttemplate.ProductTemplateInfo;
import com.artiexh.model.rest.producttemplate.request.UpdateProductTemplateDetail;
import com.artiexh.model.rest.producttemplate.request.UpdateProviderConfig;
import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProviderMapper.class, ProductCategoryMapper.class, ProductAttachMapper.class, MediaMapper.class, DateTimeMapper.class}
)
public interface ProductTemplateMapper {
	@Mapping(target = "isDeleted", source = "deleted")
	ProductTemplate entityToDomain(ProductTemplateEntity entity, @Context CycleAvoidingMappingContext context);

	@Named("entityToBasicDomain")
	@Mapping(target = "providers", ignore = true)
	@Mapping(target = "productVariants", ignore = true)
	@Mapping(target = "isDeleted", source = "deleted")
	ProductTemplate entityToBasicDomain(ProductTemplateEntity entity);

	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	ProductTemplateEntity domainToEntity(ProductTemplate domain);

	@Mapping(target = "providers", ignore = true)
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	ProductTemplateEntity domainToEntity(ProductTemplate domain, @MappingTarget ProductTemplateEntity entity);

	@Mapping(target = "category", source = "categoryId")
	@Mapping(target = "isDeleted", source = "isDeleted")
	@Mapping(target = "providers", source = "businessCodes")
	@Mapping(target = "modelFile", source = "modelFileId", qualifiedByName = "idToDomain")
	ProductTemplate detailToDomain(ProductTemplateDetail detail);

	@Mapping(target = "category", source = "categoryId")
	@Mapping(target = "modelFile", source = "modelFileId", qualifiedByName = "idToDomain")
	ProductTemplate detailToDomain(UpdateProductTemplateDetail detail);

	@Mapping(target = "providers", source = "providers", qualifiedByName = "domainSetToDetailSetWithoutProductTemplates")
	@Mapping(target = "isDeleted", source = "deleted")
	ProductTemplateDetail domainToDetail(ProductTemplate domain);

	@Named("domainToInfo")
	@Mapping(target = "isDeleted", source = "deleted")
	ProductTemplateInfo domainToInfo(ProductTemplate domain);

	@IterableMapping(qualifiedByName = "domainToInfo")
	@Named("domainSetToInfoSet")
	Set<ProductTemplateInfo> domainSetToInfoSet(Set<ProductTemplate> domainSet);

	default Integer toValue(Model3DCode code) {
		return code.getValue();
	}

	default Model3DCode toModel3DCode(Integer value) {
		return Model3DCode.fromValue(value);
	}

	default ProductTemplate idToDomain(Long productId) {
		return ProductTemplate.builder().id(productId).build();
	}

	default ProductTemplate detailToDomain(UpdateProviderConfig detail) {
		ProductTemplate productTemplate = new ProductTemplate();
		productTemplate.setProviders(
			detail.getProviders().stream()
				.map(businessCode -> Provider.builder().businessCode(businessCode).build())
				.collect(Collectors.toSet())
		);

		Set<ProductVariant> variants = new HashSet<>();
		for (Map.Entry<String, Set<ProductVariantDetail.ProviderConfig>> entry : detail.getProviderConfigs().entrySet()) {
			Long variantId = Long.parseLong(entry.getKey());
			if (!entry.getValue().stream()
				.map(ProductVariantDetail.ProviderConfig::getBusinessCode)
				.collect(Collectors.toSet())
				.equals(detail.getProviders())) {
				throw new IllegalArgumentException("All providers must be supported in variant " + variantId);
			}
			ProductVariant variant = ProductVariant.builder()
				.id(variantId)
				.providerConfigs(
					entry.getValue().stream()
						.map(providerConfig -> ProductVariantProvider.builder()
							.variantId(variantId)
							.basePriceAmount(providerConfig.getBasePriceAmount())
							.businessCode(providerConfig.getBusinessCode())
							.manufacturingTime(providerConfig.getManufacturingTime())
							.minQuantity(providerConfig.getMinQuantity())
							.build())
						.collect(Collectors.toSet())
				)
				.build();

			variants.add(variant);
		}

		productTemplate.setProductVariants(variants);
		return productTemplate;
	}
}
