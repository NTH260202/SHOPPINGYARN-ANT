package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.ProductVariantProviderEntity;
import com.artiexh.data.jpa.entity.embededmodel.ProductVariantProviderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantProviderRepository extends JpaRepository<ProductVariantProviderEntity, ProductVariantProviderId>, JpaSpecificationExecutor<ProductVariantProviderEntity> {
}
