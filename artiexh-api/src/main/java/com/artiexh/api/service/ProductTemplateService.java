package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.ProductTemplateEntity;
import com.artiexh.model.domain.ProductTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ProductTemplateService {
	ProductTemplate create(ProductTemplate product);

	ProductTemplate update(ProductTemplate product);

	Page<ProductTemplate> getInPage(Specification<ProductTemplateEntity> specification, Pageable pageable);

	ProductTemplate getById(Long id);

	ProductTemplate updateProductTemplateConfig(ProductTemplate product);

	void delete(Long id);
}
