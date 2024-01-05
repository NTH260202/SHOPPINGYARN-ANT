package com.artiexh.api.service.productinventory;

import com.artiexh.data.jpa.entity.ProductInCampaignEntity;
import com.artiexh.data.jpa.entity.ProductInventoryEntity;
import com.artiexh.model.domain.ProductInventory;
import com.artiexh.model.domain.ProductInventoryQuantity;
import com.artiexh.model.domain.SourceCategory;
import com.artiexh.model.rest.product.request.UpdateProductQuantitiesRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface ProductInventoryJpaService {
	Page<ProductInventory> getInPage(Specification<ProductInventoryEntity> specification, Pageable pageable);

	ProductInventory getDetail(String productCode);

	ProductInventory update(ProductInventory product);

	void updateQuantities(UpdateProductQuantitiesRequest productQuantity);

	ProductInventory create(Long ownerId, ProductInventory product, ProductInCampaignEntity productInCampaign);

	void updateQuantityFromCampaignRequest(Long sourceId, String sourceName, Set<ProductInventoryQuantity> productQuantities);

	void reduceQuantity(Long sourceId, String sourceName, SourceCategory sourceCategory, Set<ProductInventoryQuantity> productQuantities);

	void refundQuantity(Long sourceId, String sourceName, SourceCategory sourceCategory, Set<ProductInventoryQuantity> productQuantities);

	void delete(String productCode);
}
