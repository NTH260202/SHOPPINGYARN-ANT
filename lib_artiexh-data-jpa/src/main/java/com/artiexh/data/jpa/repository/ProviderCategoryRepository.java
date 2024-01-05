package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProviderCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCategoryRepository extends JpaRepository<ProviderCategoryEntity, Long> {
	Page<ProviderCategoryEntity> findAllByNameContaining(String name, Pageable pageable);
}
