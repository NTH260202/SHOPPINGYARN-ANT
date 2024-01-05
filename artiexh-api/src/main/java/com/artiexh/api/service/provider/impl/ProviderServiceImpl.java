package com.artiexh.api.service.provider.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.provider.ProviderService;
import com.artiexh.data.jpa.entity.CustomProductEntity;
import com.artiexh.data.jpa.entity.ProductVariantEntity;
import com.artiexh.data.jpa.entity.ProviderEntity;
import com.artiexh.data.jpa.repository.CustomProductRepository;
import com.artiexh.data.jpa.repository.ProviderRepository;
import com.artiexh.model.domain.Provider;
import com.artiexh.model.mapper.CycleAvoidingMappingContext;
import com.artiexh.model.mapper.ProviderMapper;
import com.artiexh.model.rest.campaign.response.CampaignProviderResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {
	private final ProviderMapper providerMapper;
	private final ProviderRepository providerRepository;
	private final CustomProductRepository customProductRepository;

	@Override
	@Transactional
	public Provider create(Provider provider) {
		providerRepository.findById(provider.getBusinessCode()).ifPresent(entity -> {
			throw new EntityExistsException();
		});
		ProviderEntity entity = providerMapper.domainToEntity(provider);
		providerRepository.save(entity);
		return provider;
	}

	@Override
	@Transactional
	public Provider update(Provider provider) {
		ProviderEntity entity = providerRepository.findById(provider.getBusinessCode())
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.PROVIDER_NOT_FOUND.getMessage()));
		entity = providerMapper.domainToEntity(provider, entity);
		providerRepository.save(entity);
		return provider;
	}

	@Override
	public Provider getById(String businessCode) {
		ProviderEntity provider = providerRepository.findById(businessCode).orElseThrow(EntityNotFoundException::new);
		return providerMapper.entityToDomain(provider, new CycleAvoidingMappingContext());
	}

	@Override
	public Page<Provider> getInPage(Pageable pageable) {
		Page<ProviderEntity> page = providerRepository.findAll(pageable);
		return page.map(item -> providerMapper.entityToDomain(item, new CycleAvoidingMappingContext()));
	}

	@Override
	public Page<Provider> getInPage(Specification<ProviderEntity> specification, Pageable pageable) {
		Page<ProviderEntity> page = providerRepository.findAll(specification, pageable);
		return page.map(item -> providerMapper.entityToDomain(item, new CycleAvoidingMappingContext()));
	}

	@Override
	public Set<CampaignProviderResponse> getAllSupportedCustomProducts(Long artistId, Set<Long> customProductIds) {
		var customProductEntities = customProductRepository.findAllById(customProductIds);

		for (var customProductEntity : customProductEntities) {
			if (!customProductEntity.getArtist().getId().equals(artistId)) {
				throw new InvalidException(ErrorCode.CUSTOM_PRODUCT_OWNER_INVALID, ErrorCode.CUSTOM_PRODUCT_OWNER_INVALID.getMessage() + customProductEntity.getId());
			}
		}

		var variantIds = customProductEntities.stream()
			.map(CustomProductEntity::getVariant)
			.map(ProductVariantEntity::getId)
			.collect(Collectors.toSet());

		var providerEntities = providerRepository.findAllByProductVariantIds(variantIds);

		return providerEntities.stream()
			.map(providerEntity -> {
				var providerConfigs = providerEntity.getProductVariantConfigs()
					.stream()
					.collect(Collectors.toMap(
						productVariantProviderEntity -> productVariantProviderEntity.getId().getProductVariantId(),
						providerMapper::entityToCampaignProviderConfig
					));
				var response = providerMapper.entityToCampaignProviderResponse(providerEntity);
				response.setCustomProducts(customProductEntities.stream()
					.filter(designItemEntity -> providerConfigs.containsKey(designItemEntity.getVariant().getId()))
					.map(customProductEntity -> CampaignProviderResponse.CustomProduct.builder()
						.id(customProductEntity.getId())
						.name(customProductEntity.getName())
						.config(providerConfigs.get(customProductEntity.getVariant().getId()))
						.build())
					.collect(Collectors.toSet()));
				return response;
			})
			.collect(Collectors.toSet());
	}

	@Override
	@Transactional
	public void delete(String businessCode) {
		ProviderEntity provider = providerRepository.findById(businessCode).orElseThrow(EntityNotFoundException::new);
		providerRepository.delete(businessCode);
	}

}
