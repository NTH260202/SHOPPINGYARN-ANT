package com.artiexh.api.service.provider;

import com.artiexh.model.domain.ProviderCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderCategoryService {
	Page<ProviderCategory> getAll(String name, Pageable pageable);

	ProviderCategory create(ProviderCategory providerCategory);

	ProviderCategory update(ProviderCategory providerCategory);

	void delete(Long id);
}
