package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
	Page<ProductCategoryEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
