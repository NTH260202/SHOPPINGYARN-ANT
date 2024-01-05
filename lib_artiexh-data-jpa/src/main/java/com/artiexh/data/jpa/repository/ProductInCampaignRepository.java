package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInCampaignRepository extends JpaRepository<ProductInCampaignEntity, Long> {
	Page<ProductInCampaignEntity> findAllByCampaignId(Long campaignId, Pageable pageable);
	Optional<ProductInCampaignEntity> findAllByCampaignIdAndId(Long campaignId, Long id);
}
