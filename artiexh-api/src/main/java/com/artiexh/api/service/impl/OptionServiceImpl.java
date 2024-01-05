package com.artiexh.api.service.impl;

import com.artiexh.api.service.OptionService;
import com.artiexh.data.jpa.entity.ProductOptionEntity;
import com.artiexh.data.jpa.entity.ProductOptionTemplateEntity;
import com.artiexh.data.jpa.entity.ProductVariantCombinationEntity;
import com.artiexh.data.jpa.repository.OptionTemplateRepository;
import com.artiexh.data.jpa.repository.ProductOptionRepository;
import com.artiexh.data.jpa.repository.VariantCombinationRepository;
import com.artiexh.model.domain.ProductOption;
import com.artiexh.model.mapper.ProductOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
	private final ProductOptionRepository productOptionRepository;
	private final OptionTemplateRepository optionTemplateRepository;
	private final ProductOptionMapper productOptionMapper;
	private final VariantCombinationRepository variantCombinationRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<ProductOption> getAll(Specification<ProductOptionEntity> specification, Pageable pageable) {
		Page<ProductOptionEntity> options = productOptionRepository.findAll(specification, pageable);
		return options.map(productOptionMapper::entityToDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProductOption> getAllTemplate(Specification<ProductOptionTemplateEntity> specification, Pageable pageable) {
		Page<ProductOptionTemplateEntity> options = optionTemplateRepository.findAll(pageable);
		return options.map(productOptionMapper::entityToDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Set<String>> getActiveVariantOption(Long productTemplateId, Long[] optionValueIds) {
		Map<String, Set<String>> activeOptions = new HashMap<>();
		Set<ProductVariantCombinationEntity> matchedCombination = new HashSet<>();

		if (optionValueIds != null) {
			List<ProductVariantCombinationEntity> variantCombinationEntities =
				variantCombinationRepository.findAllUniqueCombinationsByProductTemplateId(productTemplateId, optionValueIds, optionValueIds.length);

			for (ProductVariantCombinationEntity variantCombination : variantCombinationEntities) {
				matchedCombination.addAll(variantCombination.getProductVariant().getVariantCombinations());
			}
		} else {
			matchedCombination.addAll(variantCombinationRepository.findAllUniqueCombinationsByProductTemplateId(productTemplateId));
		}


		for (ProductVariantCombinationEntity variantCombinationEntity : matchedCombination) {
			if (activeOptions.containsKey(variantCombinationEntity.getOptionId().toString())) {
				activeOptions.get(variantCombinationEntity.getOptionId().toString())
					.add(variantCombinationEntity.getId().getOptionValueId().toString());
			} else {
				activeOptions.put(
					variantCombinationEntity.getOptionId().toString(),
					Stream.of(variantCombinationEntity.getId().getOptionValueId().toString()).collect(Collectors.toSet())
				);
			}
		}
		return activeOptions;
	}
}
