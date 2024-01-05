package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductVariantCombinationEntity;
import com.artiexh.data.jpa.entity.ProductVariantCombinationEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantCombinationRepository extends JpaRepository<ProductVariantCombinationEntity, ProductVariantCombinationEntityId> {

	@Query(nativeQuery = true,
		value = """
			select pvm.*
			from product_variant_combination pvm
			    inner join product_variant pv on pv.id = pvm.variant_id
			where product_template_id = :productTemplateId
			  and option_value_id in :optionValueIds
			group by variant_id
			having count(option_value_id) = :numOfOptionValue
			""")
	List<ProductVariantCombinationEntity> findAllUniqueCombinationsByProductTemplateId(
		@Param("productTemplateId") Long productTemplateId,
		@Param("optionValueIds") Long[] optionValueIds,
		@Param("numOfOptionValue") Integer numOfOptionValue);

	@Query(nativeQuery = true,
		value = """
			select pvm.*
			from product_variant_combination pvm
			    inner join product_variant pv on pv.id = pvm.variant_id
			where product_template_id = :productTemplateId
			group by option_value_id
			""")
	List<ProductVariantCombinationEntity> findAllUniqueCombinationsByProductTemplateId(
		@Param("productTemplateId") Long productTemplateId);
}
