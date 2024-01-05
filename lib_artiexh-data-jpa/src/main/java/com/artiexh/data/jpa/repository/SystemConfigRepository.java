package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.SystemConfigEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, String> {
	Page<SystemConfigEntity> findAllByKeyContains(String keyword, Pageable pageable);
}
