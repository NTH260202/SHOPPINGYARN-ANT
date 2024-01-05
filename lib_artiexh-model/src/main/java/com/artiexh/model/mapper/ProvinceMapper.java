package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ProvinceEntity;
import com.artiexh.model.domain.Province;
import com.artiexh.model.rest.address.ProvinceResponse;
import org.mapstruct.*;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProvinceMapper {

	Province entityToDomain(ProvinceEntity provinceEntity, @Context CycleAvoidingMappingContext context);

	@Mapping(target = "countryId", source = "country.id")
	@Mapping(target = "countryName", source = "country.name")
	@Mapping(target = "fullAddress", expression = "java(provinceEntity.getName() + \", \" + provinceEntity.getCountry().getName())")
	ProvinceResponse entityToResponse(ProvinceEntity provinceEntity);

	@Named("provinceEntityToDomain")
	@Mapping(target = "country.provinces", ignore = true)
	Province provinceEntityToDomain(ProvinceEntity provinceEntity);

}
