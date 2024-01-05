package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductHistoryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHistoryDetailRepository extends JpaRepository<ProductHistoryDetailEntity, Long>, JpaSpecificationExecutor<ProductHistoryDetailEntity> {
}
