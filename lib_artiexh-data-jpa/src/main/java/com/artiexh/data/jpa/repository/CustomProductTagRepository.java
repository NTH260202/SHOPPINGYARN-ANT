package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CustomProductTagEntity;
import com.artiexh.data.jpa.entity.CustomProductTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomProductTagRepository extends JpaRepository<CustomProductTagEntity, CustomProductTagId> {
}
