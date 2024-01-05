package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Long>, JpaSpecificationExecutor<CollectionEntity> {
}
