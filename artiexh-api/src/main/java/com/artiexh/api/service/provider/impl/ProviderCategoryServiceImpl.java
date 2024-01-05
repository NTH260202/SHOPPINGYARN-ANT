package com.artiexh.api.service.provider.impl;

import com.artiexh.api.service.provider.ProviderCategoryService;
import com.artiexh.data.jpa.repository.ProviderCategoryRepository;
import com.artiexh.model.domain.ProviderCategory;
import com.artiexh.model.mapper.ProviderCategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProviderCategoryServiceImpl implements ProviderCategoryService {
	private final ProviderCategoryRepository providerCategoryRepository;
	private final ProviderCategoryMapper providerCategoryMapper;

	@Override
	public Page<ProviderCategory> getAll(String name, Pageable pageable) {
		if (name != null) {
			return providerCategoryRepository.findAllByNameContaining(name, pageable)
				.map(providerCategoryMapper::entityToDomain);
		}
		return providerCategoryRepository.findAll(pageable)
			.map(providerCategoryMapper::entityToDomain);
	}

	@Override
	@Transactional
	public ProviderCategory create(ProviderCategory providerCategory) {
		var savedEntity = providerCategoryRepository.save(providerCategoryMapper.domainToEntity(providerCategory));
		return providerCategoryMapper.entityToDomain(savedEntity);
	}

	@Override
	@Transactional
	public ProviderCategory update(ProviderCategory providerCategory) {
		var existedEntity = providerCategoryRepository.findById(providerCategory.getId())
			.orElseThrow(() -> new EntityNotFoundException("Provider id " + providerCategory.getId() + " category not existed"));
		existedEntity.setName(providerCategory.getName());
		existedEntity.setImageUrl(providerCategory.getImageUrl());
		return providerCategoryMapper.entityToDomain(existedEntity);
	}

	@Override
	public void delete(Long id) {
		if (providerCategoryRepository.existsById(id)) {
			providerCategoryRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Provider id " + id + " category not existed");
		}
	}

}
