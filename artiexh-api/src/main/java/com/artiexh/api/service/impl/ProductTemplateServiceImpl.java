package com.artiexh.api.service.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.service.ProductTemplateService;
import com.artiexh.api.service.ProductVariantService;
import com.artiexh.data.jpa.entity.*;
import com.artiexh.data.jpa.repository.*;
import com.artiexh.model.domain.ProductTemplate;
import com.artiexh.model.domain.ProductVariant;
import com.artiexh.model.domain.Provider;
import com.artiexh.model.mapper.CycleAvoidingMappingContext;
import com.artiexh.model.mapper.ProductTemplateMapper;
import com.artiexh.model.mapper.ProviderMapper;
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
@RequiredArgsConstructor
public class ProductTemplateServiceImpl implements ProductTemplateService {
	private final ProductTemplateMapper productTemplateMapper;
	private final ProductTemplateRepository productTemplateRepository;
	private final ProductOptionRepository productOptionRepository;
	private final OptionValueRepository optionValueRepository;
	private final ProviderRepository providerRepository;
	private final MediaRepository mediaRepository;
	private final ProviderMapper providerMapper;
	private final ProductVariantService variantService;

	@Override
	@Transactional
	public ProductTemplate create(ProductTemplate product) {
		Set<String> businessCodes = product.getProviders().stream().map(Provider::getBusinessCode).collect(Collectors.toSet());
		if (businessCodes.size() != providerRepository.countByBusinessCodeIn(businessCodes)) {
			throw new IllegalArgumentException(ErrorCode.PROVIDER_NOT_FOUND.getMessage());
		}

		ProductTemplateEntity entity = productTemplateMapper.domainToEntity(product);

		entity.setHasVariant(false);
		entity.setModelFile(mediaRepository.getReferenceById(product.getModelFile().getId()));

		entity = productTemplateRepository.save(entity);

		for (ProductOptionEntity productOption : entity.getProductOptions()) {
			productOption.setProductTemplateId(entity.getId());
			productOption = productOptionRepository.save(productOption);

			for (OptionValueEntity optionValue : productOption.getOptionValues()) {
				optionValue.setOption(ProductOptionEntity.builder().id(productOption.getId()).build());
				optionValueRepository.save(optionValue);
			}
		}

		product = productTemplateMapper.entityToDomain(entity, new CycleAvoidingMappingContext());
		return product;
	}

	@Override
	@Transactional
	public ProductTemplate update(ProductTemplate product) {

		ProductTemplateEntity entity = productTemplateRepository.findById(product.getId())
			.orElseThrow(EntityNotFoundException::new);

		entity = productTemplateMapper.domainToEntity(product, entity);

		entity.setModelFile(MediaEntity.builder().id(product.getModelFile().getId()).build());
		entity.setCategory(ProductCategoryEntity.builder().id(product.getCategory().getId()).build());

		for (ProductOptionEntity option : entity.getProductOptions()) {
			option.setProductTemplateId(entity.getId());
			productOptionRepository.save(option);

			for (OptionValueEntity optionValue : option.getOptionValues()) {
				optionValue.setOption(ProductOptionEntity.builder().id(option.getId()).build());
				optionValueRepository.save(optionValue);
			}
		}

		productTemplateRepository.save(entity);

		product = productTemplateMapper.entityToDomain(entity, new CycleAvoidingMappingContext());
		return product;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductTemplate> getInPage(Specification<ProductTemplateEntity> specification, Pageable pageable) {
		Page<ProductTemplateEntity> entities = productTemplateRepository.findAll(specification, pageable);
		return entities.map(entity -> productTemplateMapper.entityToDomain(entity, new CycleAvoidingMappingContext()));
	}

	@Override
	@Transactional(readOnly = true)
	public ProductTemplate getById(Long id) {
		ProductTemplateEntity entity = productTemplateRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
		return productTemplateMapper.entityToDomain(entity, new CycleAvoidingMappingContext());
	}

	@Override
	@Transactional
	public ProductTemplate updateProductTemplateConfig(ProductTemplate product) {
		ProductTemplateEntity entity = productTemplateRepository.findById(product.getId())
			.orElseThrow(EntityNotFoundException::new);

		for (ProductVariantEntity variant : entity.getProvidedModels()) {
			boolean isMatchedAllVariant = product.getProductVariants().stream().anyMatch(existedVariant -> existedVariant.getId().equals(variant.getId()));
			if (!isMatchedAllVariant) {
				throw new IllegalArgumentException(ErrorCode.VARIANT_NOT_FOUND.getMessage() + variant.getId());
			}
		}

		entity.setProviders(
			product.getProviders().stream()
				.map(providerMapper::domainToEntity)
				.collect(Collectors.toSet())
		);

		productTemplateRepository.save(entity);

		for (ProductVariant variant : product.getProductVariants()) {
			variantService.updateProviderConfig(variant.getId(), variant.getProviderConfigs());
		}

		return productTemplateMapper.entityToDomain(entity, new CycleAvoidingMappingContext());
	}

	@Override
	@Transactional
	public void delete(Long id) {
		ProductTemplateEntity entity = productTemplateRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
		productTemplateRepository.delete(id);
	}
}
