package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CampaignSaleEntity;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CampaignSaleRepository
	extends JpaRepository<CampaignSaleEntity, Long>, JpaSpecificationExecutor<CampaignSaleEntity> {
	Optional<CampaignSaleEntity> findCampaignSaleEntityByIdAndOwnerId(Long id, Long ownerId);

	@QueryHints(value = {
		@QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1"),
		@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "false"),
	})
	Stream<CampaignSaleEntity> streamAllByStatusAndToBefore(byte status, Instant to);
}
