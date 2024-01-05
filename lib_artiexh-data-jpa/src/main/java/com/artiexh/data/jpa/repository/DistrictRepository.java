package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.DistrictEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<DistrictEntity, Integer> {

	Page<DistrictEntity> findAllByProvinceId(Short provinceId, Pageable pageable);

}
