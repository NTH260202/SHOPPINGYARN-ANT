package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.OptionValueEntity;
import com.artiexh.data.jpa.entity.ProductOptionEntity;
import com.artiexh.data.jpa.entity.ProductOptionTemplateEntity;
import com.artiexh.data.jpa.entity.embededmodel.OptionConfig;
import com.artiexh.model.domain.OptionValue;
import com.artiexh.model.domain.ProductOption;
import com.artiexh.model.domain.VariantCombination;
import com.artiexh.model.rest.option.OptionDetail;
import com.artiexh.model.rest.option.OptionValueDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductOptionMapper {
	ProductOption entityToDomain(ProductOptionEntity entity);

	ProductOptionEntity domainToEntity(ProductOption option);

	OptionValueEntity domainToEntity(OptionValue optionValue);

	ProductOption entityToDomain(ProductOptionTemplateEntity entity);

	Set<ProductOption> entitySetToDomainSet(List<ProductOptionEntity> entity);

	ProductOption optionConfigToProductOption(OptionConfig optionConfig);

	OptionDetail domainToDetail(ProductOption productOption);

	OptionValueDetail domainToDetail(OptionValue optionValue);

	default Set<VariantCombination> optionsToVariantCombinations(List<ProductOptionEntity> options) {
		Set<VariantCombination> variants = new HashSet<>();
		for (ProductOptionEntity option : options) {
			for (OptionValueEntity optionValue : option.getOptionValues()) {
				VariantCombination combination = VariantCombination.builder()
					.optionId(option.getId())
					.optionValueId(optionValue.getId())
					.isOptional(option.getIsOptional())
					.build();
				variants.add(combination);
			}
		}
		return variants;
	}
}
