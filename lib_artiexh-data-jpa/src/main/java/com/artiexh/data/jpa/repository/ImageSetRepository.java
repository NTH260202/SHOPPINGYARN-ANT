package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ImageSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageSetRepository extends JpaRepository<ImageSetEntity, Long> {
}
