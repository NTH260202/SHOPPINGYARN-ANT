package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductInventoryEntity;
import com.artiexh.data.jpa.projection.ProductInventoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventoryEntity, String>, JpaSpecificationExecutor<ProductInventoryEntity> {

	@Modifying
	@Query("update ProductInventoryEntity productInventory " +
		"set productInventory.quantity = productInventory.quantity + :quantity " +
		"where productInventory.productCode = :productCode")
	void updateQuantity(@Param("productCode") String productCode, @Param("quantity") Long quantity);

	@Query(
		nativeQuery = true,
		value = """
			select pi.product_code as productCode, pi.product_in_campaign_id as productInCampaignId
			from product_inventory pi
			join product_in_campaign pic on pi.product_in_campaign_id = pic.id
			where pic.campaign_id = :campaignId
			"""
	)
	Set<ProductInventoryCode> getAllByCampaignId(@Param("campaignId") Long campaignId);

	int countAllByProductCodeInAndOwnerId(Collection<String> productCodes, Long ownerId);

	boolean existsByProductCodeAndOwnerId(String productCode, Long ownerId);

	boolean existsAllByProductCodeIn(Collection<String> productCode);

	Set<ProductInventoryEntity> findAllByProductInCampaignIdIn(Set<Long> productInCampaignIds);

	Optional<ProductInventoryEntity> findByProductCodeAndIsDeleted(String productCode, boolean deleted);
}
