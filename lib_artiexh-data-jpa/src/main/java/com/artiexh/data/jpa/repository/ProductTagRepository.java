package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductTagRepository extends JpaRepository<ProductTagEntity, Long> {
	Set<ProductTagEntity> findAllByNameIn(Set<String> names);
}
