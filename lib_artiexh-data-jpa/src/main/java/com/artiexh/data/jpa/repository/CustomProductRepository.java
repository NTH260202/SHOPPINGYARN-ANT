package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CustomProductEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomProductRepository extends JpaRepository<CustomProductEntity, Long>, JpaSpecificationExecutor<CustomProductEntity> {
	Optional<CustomProductEntity> findByIdAndArtistId(Long id, Long artistId);

	@Modifying
	@Query(value = "update CustomProductEntity item set item.isDeleted = true where item.id = :id")
	void deleteById(@NotNull @Param("id") Long id);
}
