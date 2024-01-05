package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CustomProductTagEntity;
import com.artiexh.data.jpa.entity.ProductTagEntity;
import com.artiexh.model.domain.ProductTag;
import com.artiexh.model.rest.tag.ProductTagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductTagMapper {

	ProductTag entityToDomain(ProductTagEntity productTagEntity);
	ProductTag entityToDomain(CustomProductTagEntity productTagEntity);

	ProductTagEntity domainToEntity(ProductTag productTag);

	ProductTagResponse entityToResponse(ProductTagEntity productTag);

	default String productTagToString(ProductTag tag) {
		if (tag == null) {
			return null;
		}
		return tag.getName();
	}

	default ProductTag stringToProductTag(String tag) {
		if (tag == null) {
			return null;
		}
		ProductTag productTag = new ProductTag();
		productTag.setName(tag);
		return productTag;
	}

//	default Set<ProductTag> tagNamesToTagDomainS(Set<String> tag) {
//		Set<ProductTag> result = new HashSet<>();
//
//		if (tag != null) {
//			for (String name : tag) {
//				result.add(tagResponseToTagDomain(name));
//			}
//		}
//
//		return result;
//	}

}
