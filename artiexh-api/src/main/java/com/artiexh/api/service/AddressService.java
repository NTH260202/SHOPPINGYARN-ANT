package com.artiexh.api.service;

import com.artiexh.model.domain.Country;
import com.artiexh.model.domain.District;
import com.artiexh.model.domain.Province;
import com.artiexh.model.domain.Ward;
import com.artiexh.model.rest.address.ProvinceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {

	Page<ProvinceResponse> getInPage(Pageable pageable);

	Page<Country> getCountries(Pageable pageable);

	Page<Province> getProvinces(Short countryId, Pageable pageable);

	Page<District> getDistricts(Short provinceId, Pageable pageable);

	Page<Ward> getWards(Short districtId, Pageable pageable);
}
