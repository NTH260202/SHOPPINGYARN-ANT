package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProvinceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<ProvinceEntity, Integer> {

	Page<ProvinceEntity> findAllByCountryId(Short countryId, Pageable pageable);

}
