package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CampaignOrderRepository extends JpaRepository<CampaignOrderEntity, Long>, JpaSpecificationExecutor<CampaignOrderEntity> {

	Optional<CampaignOrderEntity> findByIdAndCampaignSaleOwnerId(Long orderId, Long artistId);


	Optional<CampaignOrderEntity> findByIdAndOrderUserId(Long orderId, Long userId);

	@Modifying
	@Query("update CampaignOrderEntity set status = cast(1 as byte) where order.id = :id")
	void updatePayment(@Param("id") Long id);

	Set<CampaignOrderEntity> getAllByOrderId(Long orderId);
}
