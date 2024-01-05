package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ImageSetEntity;
import com.artiexh.data.jpa.entity.MediaEntity;
import com.artiexh.model.domain.ImageSet;
import com.artiexh.model.domain.Media;
import com.artiexh.model.rest.customproduct.CustomProductDesignRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MediaMapper {
	ImageSet entityToDomain(ImageSetEntity entity);

	Media domainToEntity(MediaEntity entity);

	@Mapping(target = "mockupImage", source = "mockupImageId", qualifiedByName = "idToDomain")
	@Mapping(target = "manufacturingImage", source = "manufacturingImageId", qualifiedByName = "idToDomain")
	ImageSet detailToDomain(CustomProductDesignRequest.ImageSet imageSet);

	@Mapping(target = "mockupImage.id", source = "mockupImageId")
	@Mapping(target = "manufacturingImage.id", source = "manufacturingImageId")
	ImageSetEntity detailToEntity(CustomProductDesignRequest.ImageSet imageSet);

	ImageSetEntity domainToEntity(ImageSet imageSet);

	CustomProductDesignRequest.ImageSet domainToDetail(ImageSet imageSet);

	@Named("idToDomain")
	Media idToDomain(Long id);

	@Named("idToEntity")
	MediaEntity idToEntity(Long id);
}
