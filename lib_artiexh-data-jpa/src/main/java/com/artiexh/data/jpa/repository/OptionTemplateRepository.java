package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductOptionTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionTemplateRepository extends JpaRepository<ProductOptionTemplateEntity, Long> {
}
