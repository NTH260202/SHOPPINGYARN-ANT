package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistoryEntity, Long>, JpaSpecificationExecutor<ProductHistoryEntity> {
}
