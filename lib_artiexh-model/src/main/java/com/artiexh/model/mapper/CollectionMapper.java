package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CollectionEntity;
import com.artiexh.model.domain.Collection;
import com.artiexh.model.rest.collection.CollectionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ProductVariantMapper.class}
)
public interface CollectionMapper {
	CollectionDetail domainToDetail(Collection collection);

	Collection detailToDomain(CollectionDetail collection);

	Collection entityToDomain(CollectionEntity collection);

	CollectionEntity domainToEntity(Collection collection);
}
