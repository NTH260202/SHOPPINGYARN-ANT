package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductCategoryEntity;
import com.artiexh.model.domain.ProductCategory;
import com.artiexh.model.rest.category.ProductCategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductCategoryMapper {
	@Named("idToDomain")
	ProductCategory idToDomain(Long id);

	ProductCategory entityToDomain(ProductCategoryEntity productCategoryEntity);

	ProductCategoryResponse entityToResponse(ProductCategoryEntity entity);

	ProductCategoryResponse domainToResponse(ProductCategory entity);

	default ProductCategoryEntity domainToEntity(Long categoryId) {
		return ProductCategoryEntity.builder().id(categoryId).build();
	}

}
