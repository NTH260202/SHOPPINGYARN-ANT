package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.ProductVariantEntity;
import com.artiexh.model.domain.ProductVariant;
import com.artiexh.model.domain.ProductVariantProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface ProductVariantService {
	ProductVariant create(ProductVariant product);

	Set<ProductVariant> create(Set<ProductVariant> product, Long productTemplateId);

	ProductVariant update(ProductVariant product);

	ProductVariant updateProviderConfig(Long id, Set<ProductVariantProvider> providerConfigs);

	void delete(Long id);

	ProductVariant getById(Long id);

	Page<ProductVariant> getAll(Specification<ProductVariantEntity> specification, Pageable pageable);

	Page<ProductVariant> getAll(Long productTemplateIds, Set<Long> optionValueIds, Pageable pageable);
}
