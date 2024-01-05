package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.MediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {
	Optional<MediaEntity> findByIdAndOwnerId(Long id, Long ownerId);


	@Query(
		nativeQuery = true,
		value = """
			SELECT m.*
			FROM media m
			LEFT JOIN account_media_mapping amm on m.id = amm.media_id
			LEFT JOIN account a on m.owner_id = a.id
			WHERE m.id = :id AND (m.owner_id = :userId OR amm.shared_user_id = :userId OR a.role = 0);"""
	)
	Optional<MediaEntity> findByIdAndSharedUsersId(@Param("id") Long id, @Param("userId") Long userId);
}
