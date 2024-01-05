package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.UserAddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

	Page<UserAddressEntity> findByUserId(Long userId, Pageable pageable);

	Optional<UserAddressEntity> findByIdAndUserId(Long id, Long userId);

	@Modifying
	@Query("update UserAddressEntity set isDefault = false where user.id = :userId")
	void setAllNotDefaultByUserId(Long userId);

	boolean existsByUserId(Long userId);

}
