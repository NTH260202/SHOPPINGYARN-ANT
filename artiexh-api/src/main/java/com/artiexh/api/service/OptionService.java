package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.ProductOptionEntity;
import com.artiexh.data.jpa.entity.ProductOptionTemplateEntity;
import com.artiexh.model.domain.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Set;

public interface OptionService {
	Page<ProductOption> getAll(Specification<ProductOptionEntity> specification, Pageable pageable);

	Page<ProductOption> getAllTemplate(Specification<ProductOptionTemplateEntity> specification, Pageable pageable);

	Map<String, Set<String>> getActiveVariantOption(Long productTemplateId, Long[] optionValueIds);
}
