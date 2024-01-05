package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.WardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<WardEntity, Integer> {

	Page<WardEntity> findAllByDistrictId(Short districtId, Pageable pageable);

}
