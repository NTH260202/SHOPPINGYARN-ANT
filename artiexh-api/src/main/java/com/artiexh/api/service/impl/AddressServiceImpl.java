package com.artiexh.api.service.impl;

import com.artiexh.api.service.AddressService;
import com.artiexh.data.jpa.repository.CountryRepository;
import com.artiexh.data.jpa.repository.DistrictRepository;
import com.artiexh.data.jpa.repository.ProvinceRepository;
import com.artiexh.data.jpa.repository.WardRepository;
import com.artiexh.model.domain.Country;
import com.artiexh.model.domain.District;
import com.artiexh.model.domain.Province;
import com.artiexh.model.domain.Ward;
import com.artiexh.model.mapper.AddressMapper;
import com.artiexh.model.mapper.ProvinceMapper;
import com.artiexh.model.rest.address.ProvinceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
	private final CountryRepository countryRepository;
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final WardRepository wardRepository;
	private final ProvinceMapper provinceMapper;
	private final AddressMapper addressMapper;

	@Override
	public Page<ProvinceResponse> getInPage(Pageable pageable) {
		return provinceRepository.findAll(pageable).map(provinceMapper::entityToResponse);
	}

	@Override
	public Page<Country> getCountries(Pageable pageable) {
		return countryRepository.findAll(pageable)
			.map(addressMapper::countryEntityToCountry);
	}

	@Override
	public Page<Province> getProvinces(Short countryId, Pageable pageable) {
		return provinceRepository.findAllByCountryId(countryId, pageable)
			.map(addressMapper::provinceEntityToProvince);
	}

	@Override
	public Page<District> getDistricts(Short provinceId, Pageable pageable) {
		return districtRepository.findAllByProvinceId(provinceId, pageable)
			.map(addressMapper::districtEntityToDistrict);
	}

	@Override
	public Page<Ward> getWards(Short districtId, Pageable pageable) {
		return wardRepository.findAllByDistrictId(districtId, pageable)
			.map(addressMapper::wardEntityToWard);
	}

}
