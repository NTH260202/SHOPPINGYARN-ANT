package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProductAttachEntity;
import com.artiexh.model.domain.ProductAttach;
import com.artiexh.model.domain.ProductAttachType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductAttachMapper {

	ProductAttach entityToDomain(ProductAttachEntity productAttachEntity);

	ProductAttachEntity domainToEntity(ProductAttach productAttach);

	Set<ProductAttach> entitiesToDomains(Set<ProductAttachEntity> entities);

	default Integer toValue(ProductAttachType type) {
		return type.getValue();
	}

	default ProductAttachType toProductAttachType(Integer value) {
		return ProductAttachType.fromValue(value);
	}

}
