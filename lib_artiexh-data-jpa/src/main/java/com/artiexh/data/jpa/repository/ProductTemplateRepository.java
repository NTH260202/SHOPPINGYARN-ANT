package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductTemplateRepository extends JpaRepository<ProductTemplateEntity, Long>,
	JpaSpecificationExecutor<ProductTemplateEntity> {
	@Modifying
	@Query("update ProductTemplateEntity product " +
		"set product.hasVariant = true " +
		"where product.id = :productTemplateId and product.hasVariant = false")
	void updateVariant(@Param("productTemplateId") Long productTemplateId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update ProductTemplateEntity template set template.isDeleted = true where template.id = :id")
	void delete(@Param("id") Long id);

	Optional<ProductTemplateEntity> findProductTemplateEntityByIdAndIsDeleted(Long id, boolean deleted);
}
