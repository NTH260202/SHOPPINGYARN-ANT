package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long>, JpaSpecificationExecutor<ProductOptionEntity> {
	Optional<ProductOptionEntity> findProductOptionEntityByProductTemplateIdAndId(Long productTemplateId, Long id);

	List<ProductOptionEntity> findProductOptionEntityByProductTemplateId(Long productTemplateId);
}
