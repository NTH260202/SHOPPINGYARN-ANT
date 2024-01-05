package com.artiexh.api.service.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.service.ProductVariantService;
import com.artiexh.data.jpa.entity.*;
import com.artiexh.data.jpa.entity.embededmodel.ProductVariantProviderId;
import com.artiexh.data.jpa.repository.*;
import com.artiexh.model.domain.ProductOption;
import com.artiexh.model.domain.ProductVariant;
import com.artiexh.model.domain.ProductVariantProvider;
import com.artiexh.model.domain.VariantCombination;
import com.artiexh.model.mapper.CycleAvoidingMappingContext;
import com.artiexh.model.mapper.ProductOptionMapper;
import com.artiexh.model.mapper.ProductVariantMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
	private final ProductVariantRepository repository;
	private final ProductVariantMapper mapper;
	private final ProductOptionMapper optionMapper;
	private final ProductOptionRepository productOptionRepository;
	private final VariantCombinationRepository variantCombinationRepository;
	private final ProductVariantProviderRepository productVariantProviderRepository;
	private final ProviderRepository providerRepository;
	private final ProductTemplateRepository productTemplateRepository;

	@Override
	@Transactional
	public ProductVariant create(ProductVariant product) {

		//Validate provider
		int allowedProviders = providerRepository.countProvider(product.getProductTemplateId(), product.getProviderConfigs().stream()
			.map(ProductVariantProvider::getBusinessCode)
			.toList());

		if (allowedProviders != product.getProviderConfigs().size()) {
			throw new IllegalArgumentException(ErrorCode.PROVIDER_INVALID.getMessage());
		}

		//Validation option id and option value
		if (!product.getVariantCombinations().isEmpty()) {
			List<ProductOptionEntity> existedOptions = productOptionRepository.findProductOptionEntityByProductTemplateId(product.getProductTemplateId());
			validateOptions(existedOptions, product.getVariantCombinations());
		}

		ProductVariantEntity entity = mapper.domainToEntity(product);
		repository.save(entity);

		for (VariantCombination combination : product.getVariantCombinations()) {
			ProductVariantCombinationEntity combinationEntity = mapper.domainToEntity(combination);
			combinationEntity.setId(ProductVariantCombinationEntityId.builder()
				.optionValueId(combination.getOptionValueId())
				.variantId(entity.getId())
				.build());
			combinationEntity.setProductVariant(entity);
			variantCombinationRepository.save(combinationEntity);
		}

		for (ProductVariantProvider providerConfig : product.getProviderConfigs()) {
			ProductVariantProviderEntity provider = mapper.domainToEntity(providerConfig);
			provider.setId(ProductVariantProviderId.builder()
				.businessCode(providerConfig.getBusinessCode())
				.productVariantId(entity.getId())
				.build());
			provider.setProductVariant(entity);
			provider.setProvider(providerRepository.findById(providerConfig.getBusinessCode())
				.orElseThrow(EntityNotFoundException::new));
			productVariantProviderRepository.save(provider);
		}

		product.setId(entity.getId());
		return product;
	}

	private void validateOptions(List<ProductOptionEntity> options, Set<VariantCombination> combinations) {
		Set<VariantCombination> existedCombinations = optionMapper.optionsToVariantCombinations(options);
		Set<ProductOption> existedOptions = optionMapper.entitySetToDomainSet(options);

		for (VariantCombination combination : combinations) {
			boolean isExisted = existedCombinations.stream().anyMatch(existedCombination ->
				existedCombination.getOptionValueId().equals(combination.getOptionValueId())
					&& existedCombination.getOptionId().equals(combination.getOptionId())
			);

			if (!isExisted) {
				throw new IllegalArgumentException(ErrorCode.OPTION_VALUE_INVALID.getMessage() + combination.getOptionValueId());
			}

			existedOptions.removeIf(existedOption -> existedOption.getId().equals(combination.getOptionId()));
		}

		boolean isExisted = existedOptions.stream().anyMatch(existedOption -> !existedOption.getIsOptional());

		if (isExisted) {
			throw new IllegalArgumentException(ErrorCode.REQUIRED_OPTION_NOT_FOUND.getMessage());
		}
	}

	@Override
	@Transactional
	public Set<ProductVariant> create(Set<ProductVariant> products, Long productTemplateId) {
		productTemplateRepository.findProductTemplateEntityByIdAndIsDeleted(productTemplateId, false);
		Set<ProductVariant> result = new HashSet<>();
		for (ProductVariant productVariant : products) {
			productVariant.setProductTemplateId(productTemplateId);
			result.add(create(productVariant));
		}
		productTemplateRepository.updateVariant(productTemplateId);
		return result;
	}

	@Override
	@Transactional
	public ProductVariant update(ProductVariant product) {
		ProductVariantEntity entity = repository.findById(product.getId())
			.orElseThrow(() ->
				new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage() + product.getId())
			);

		Long productTemplateId = entity.getProductTemplateId();

		//Validate provider
		int allowedProviders = providerRepository.countProvider(
			productTemplateId,
			product.getProviderConfigs().stream()
				.map(ProductVariantProvider::getBusinessCode)
				.toList()
		);

		if (allowedProviders != product.getProviderConfigs().size()) {
			throw new IllegalArgumentException(ErrorCode.PROVIDER_INVALID.getMessage());
		}

		//Validation option id and option value
		if (!product.getVariantCombinations().isEmpty()) {
			List<ProductOptionEntity> existedOptions = productOptionRepository.findProductOptionEntityByProductTemplateId(productTemplateId);
			validateOptions(existedOptions, product.getVariantCombinations());
		}

		entity = mapper.domainToEntity(product, entity);

		Set<ProductVariantCombinationEntity> combinations = new HashSet<>();
		entity.getVariantCombinations().clear();
		for (VariantCombination combination : product.getVariantCombinations()) {
			ProductVariantCombinationEntity combinationEntity = mapper.domainToEntity(combination);
			combinationEntity.setId(ProductVariantCombinationEntityId.builder()
				.optionValueId(combination.getOptionValueId())
				.variantId(entity.getId())
				.build());
			combinationEntity.setProductVariant(entity);
			variantCombinationRepository.save(combinationEntity);
			combinations.add(combinationEntity);
		}
		entity.getVariantCombinations().addAll(combinations);


		Set<ProductVariantProviderEntity> providers = new HashSet<>();
		entity.getProviderConfigs().clear();
		for (ProductVariantProvider providerConfig : product.getProviderConfigs()) {
			ProductVariantProviderEntity provider = mapper.domainToEntity(providerConfig);
			provider.setId(ProductVariantProviderId.builder()
				.businessCode(providerConfig.getBusinessCode())
				.productVariantId(entity.getId())
				.build());
			provider.setProductVariant(entity);
			provider.setProvider(providerRepository.findById(providerConfig.getBusinessCode())
				.orElseThrow(EntityNotFoundException::new));
			productVariantProviderRepository.save(provider);
			providers.add(provider);
		}
		entity.getProviderConfigs().addAll(providers);

		repository.save(entity);
		return mapper.entityToDomain(entity, new CycleAvoidingMappingContext());
	}

	@Override
	public ProductVariant updateProviderConfig(Long id, Set<ProductVariantProvider> providerConfigs) {
		ProductVariantEntity entity = repository.findById(id)
			.orElseThrow(() ->
				new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage() + id)
			);

		Set<ProductVariantProviderEntity> providers = new HashSet<>();
		entity.getProviderConfigs().clear();
		for (ProductVariantProvider providerConfig : providerConfigs) {
			ProductVariantProviderEntity provider = mapper.domainToEntity(providerConfig);
			provider.setId(ProductVariantProviderId.builder()
				.businessCode(providerConfig.getBusinessCode())
				.productVariantId(entity.getId())
				.build());
			provider.setProductVariant(entity);
			provider.setProvider(providerRepository.findById(providerConfig.getBusinessCode())
				.orElseThrow(EntityNotFoundException::new));
			productVariantProviderRepository.save(provider);
			providers.add(provider);
		}
		entity.getProviderConfigs().addAll(providers);

		repository.save(entity);
		return mapper.entityToDomain(entity, new CycleAvoidingMappingContext());
	}

	@Override
	public void delete(Long id) {
		ProductVariantEntity product = repository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
		repository.deleteById(product.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductVariant getById(Long id) {
		ProductVariantEntity entity = repository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
		return mapper.entityToDomain(entity, new CycleAvoidingMappingContext());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductVariant> getAll(Specification<ProductVariantEntity> specification, Pageable pageable) {
		Page<ProductVariantEntity> productPage = repository.findAll(specification, pageable);
		return productPage.map(product -> mapper.entityToDomain(product, new CycleAvoidingMappingContext()));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductVariant> getAll(Long productTemplateIds, Set<Long> optionValueIds, Pageable pageable) {
		Page<ProductVariantEntity> productPage;
		if (optionValueIds == null || optionValueIds.isEmpty()) {
			productPage = repository.findAllByProductTemplateId(productTemplateIds, pageable);
		} else {
			productPage = repository.findAllByOptionAndProductTemplateId(pageable, optionValueIds, optionValueIds.size());
		}
		return productPage.map(product -> mapper.entityToDomain(product, new CycleAvoidingMappingContext()));
	}
}
