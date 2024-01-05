package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.CountryEntity;
import com.artiexh.data.jpa.entity.DistrictEntity;
import com.artiexh.data.jpa.entity.ProvinceEntity;
import com.artiexh.data.jpa.entity.WardEntity;
import com.artiexh.model.domain.Country;
import com.artiexh.model.domain.District;
import com.artiexh.model.domain.Province;
import com.artiexh.model.domain.Ward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

	@Mapping(target = "provinces", ignore = true)
	Country countryEntityToCountry(CountryEntity countryEntity);

	@Mapping(target = "districts", ignore = true)
	Province provinceEntityToProvince(ProvinceEntity provinceEntity);

	@Mapping(target = "wards", ignore = true)
	District districtEntityToDistrict(DistrictEntity districtEntity);

	Ward wardEntityToWard(WardEntity wardEntity);
}
