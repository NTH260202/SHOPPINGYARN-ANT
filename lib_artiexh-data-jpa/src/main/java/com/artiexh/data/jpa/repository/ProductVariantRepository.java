package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductVariantEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long>,
	JpaSpecificationExecutor<ProductVariantEntity> {

	@Query(nativeQuery = true,
		value = """
			select pv.*
			from product_variant pv
			    inner join product_variant_combination pvm on id = pvm.variant_id
			    inner join (
			        select variant_id, count(option_value_id) as num_of_option
			        from product_variant_combination
			        where option_value_id in :optionValueIds
			        group by variant_id
			        having count(option_value_id) = :numOfOptionValue) temp on temp.variant_id = pv.id
			group by pvm.variant_id
			order by count(pvm.option_id)
			""",
		countQuery = """
			select count(pv.id)
			from product_variant pv
			         inner join product_variant_combination pvm on id = pvm.variant_id
			         inner join (
			    select variant_id, count(option_value_id) as num_of_option
			    from product_variant_combination
			    where option_value_id in :optionValueIds
			    group by variant_id
			    having count(option_value_id) = :numOfOptionValue) temp on temp.variant_id = pv.id
			group by pvm.variant_id
			order by count(pvm.option_id)
			""")
	Page<ProductVariantEntity> findAllByOptionAndProductTemplateId(
		Pageable pageable,
		@Param("optionValueIds") Set<Long> optionValueIds,
		@Param("numOfOptionValue") Integer numOfOptionValue);

	@Query(nativeQuery = true,
		value = """
			select pv.*
			from product_variant pv
			         left join product_variant_combination pvm on id = pvm.variant_id
			         where product_template_id = :productTemplateId
			         group by pvm.variant_id
			order by count(pvm.option_id)
			""",
		countQuery = """
			select count(pv.id)
			from product_variant pv
			         left join product_variant_combination pvm on id = pvm.variant_id
			         where product_template_id = :productTemplateId
			         group by pvm.variant_id
			order by count(pvm.option_id)
			""")
	Page<ProductVariantEntity> findAllByProductTemplateId(@Param("productTemplateId") @NotNull Long productTemplateId, Pageable pageable);

	List<ProductVariantEntity> findAllByProductTemplateId(@NotNull Long productTemplateId);
}
