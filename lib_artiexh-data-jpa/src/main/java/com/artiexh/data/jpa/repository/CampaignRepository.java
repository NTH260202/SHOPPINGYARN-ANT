package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEntity, Long>, JpaSpecificationExecutor<CampaignEntity> {
	Page<CampaignEntity> findAllByOwnerUsername(String ownerUsername, Pageable pageable);
}
