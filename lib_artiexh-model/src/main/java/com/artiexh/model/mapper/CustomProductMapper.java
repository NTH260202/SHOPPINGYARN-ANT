package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CustomProductEntity;
import com.artiexh.data.jpa.entity.CustomProductTagEntity;
import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import com.artiexh.data.jpa.entity.ProductVariantCombinationEntity;
import com.artiexh.model.domain.CustomProduct;
import com.artiexh.model.rest.customproduct.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Log4j2
@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		MediaMapper.class,
		ArtistMapper.class,
		ProductVariantMapper.class,
		DateTimeMapper.class,
		ProductAttachMapper.class,
		ProductTemplateMapper.class
	}
)
public abstract class CustomProductMapper {
	@Autowired
	private ObjectMapper objectMapper;

	@Mapping(target = "artist", source = "artistId", qualifiedByName = "idToDomain")
	@Mapping(target = "variant", source = "variantId", qualifiedByName = "idToDomain")
	abstract public CustomProduct detailToDomain(CustomProductGeneralRequest detail);

	abstract public CustomProductGeneralRequest domainToDetail(CustomProduct item);

	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	abstract public CustomProductEntity domainToEntity(CustomProduct item, @MappingTarget CustomProductEntity entity);

	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	abstract public CustomProductEntity domainToEntity(CustomProduct item);

	@Mapping(target = "variant.id", source = "variantId")
	@Mapping(target = "artist.id", source = "artistId")
	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "modelThumbnail", ignore = true)
	abstract public CustomProductEntity generalRequestToEntity(CustomProductGeneralRequest detail);

	@Mapping(target = "variant.id", source = "variantId")
	@Mapping(target = "artist.id", source = "artistId")
	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "modelThumbnail", ignore = true)
	@Mapping(target = "imageSet", ignore = true)
	abstract public CustomProductEntity designRequestToEntity(CustomProductDesignRequest detail);

	abstract public CustomProduct entityToDomain(CustomProductEntity entity, @Context CycleAvoidingMappingContext context);

	@Mapping(target = "variant", qualifiedByName = "entityToBasicDomain")
	abstract public CustomProduct entityToDomain(CustomProductEntity entity);

	@Mapping(target = "variant.productTemplate.modelFileId", source = "variant.productTemplate.modelFile.id")
	abstract public CustomProductResponse entityToGetAllResponse(CustomProductEntity entity);

	@Named("customProductEntityToGeneralResponse")
	@Mapping(target = "variant.productTemplate.modelFileId", source = "variant.productTemplate.modelFile.id")
	abstract public CustomProductGeneralResponse entityToGeneralResponse(CustomProductEntity entity);

	@Named("productInCampaignEntityToGeneralResponse")
	public CustomProductGeneralResponse productInCampaignEntityToGeneralResponse(ProductInCampaignEntity product) {
		if (product == null) {
			return null;
		}

		if (product.getSavedCustomProduct() != null) {
			try {
				return objectMapper.readValue(product.getSavedCustomProduct(), CustomProductGeneralResponse.class);
			} catch (JsonProcessingException e) {
				log.error("Parse savedCustomProduct to CustomProductGeneralResponse fail", e);
				return entityToGeneralResponse(product.getCustomProduct());
			}
		} else {
			return entityToGeneralResponse(product.getCustomProduct());
		}
	}

	abstract public CustomProductDesignResponse entityToDesignResponse(CustomProductEntity entity);

	@Named("customProductEntityToDetailResponse")
	@Mapping(target = "variant.productTemplate.modelFileId", source = "variant.productTemplate.modelFile.id")
	abstract public CustomProductDetailResponse entityToDetailResponse(CustomProductEntity entity);

	@Named("productInCampaignEntityToDetailResponse")
	public CustomProductDetailResponse productInCampaignEntityToDetailResponse(ProductInCampaignEntity product) {
		if (product == null) {
			return null;
		}

		if (product.getSavedCustomProduct() != null) {
			try {
				return objectMapper.readValue(product.getSavedCustomProduct(), CustomProductDetailResponse.class);
			} catch (JsonProcessingException e) {
				log.error("Parse savedCustomProduct to CustomProductDetailResponse fail", e);
				return entityToDetailResponse(product.getCustomProduct());
			}
		} else {
			return entityToDetailResponse(product.getCustomProduct());
		}
	}

	@Named("variantCombinationEntityToDomain")
	@Mapping(target = "option.id", source = "optionValue.option.id")
	@Mapping(target = "option.name", source = "optionValue.option.name")
	abstract public ProductVariantCombinationResponse variantCombinationEntityToDomain(ProductVariantCombinationEntity entity);

	@IterableMapping(qualifiedByName = "variantCombinationEntityToDomain")
	abstract public Set<ProductVariantCombinationResponse> variantCombinationsEntityToDomains(Set<ProductVariantCombinationEntity> entities);

	@Named("entityToDomainWithoutVariant")
	@Mapping(target = "variant", ignore = true)
	@Mapping(target = "artist", qualifiedByName = "basicArtistInfo")
	abstract public CustomProduct entityToDomainWithoutVariant(CustomProductEntity entity);

	public String customProductTagToTag(CustomProductTagEntity tag) {
		if (tag == null) {
			return null;
		}

		return tag.getName();
	}

}
