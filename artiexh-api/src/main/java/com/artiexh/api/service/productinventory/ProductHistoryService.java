package com.artiexh.api.service.productinventory;

import com.artiexh.data.jpa.entity.ProductHistoryDetailEntity;
import com.artiexh.data.jpa.entity.ProductHistoryEntity;
import com.artiexh.model.domain.ProductHistory;
import com.artiexh.model.domain.ProductHistoryAction;
import com.artiexh.model.domain.ProductInventoryQuantity;
import com.artiexh.model.domain.SourceCategory;
import com.artiexh.model.rest.producthistory.ProductHistoryDetailPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface ProductHistoryService {

	void create(
		ProductHistoryAction action,
		Long sourceId,
		String sourceName,
		SourceCategory sourceCategory,
		Set<ProductInventoryQuantity> productQuantities);

	Page<ProductHistory> getInPage(Pageable pageable, Specification<ProductHistoryEntity> specification);

	ProductHistory getById(Long id);

	Page<ProductHistoryDetailPageResponse> getDetailInPage(Pageable pageable, Specification<ProductHistoryDetailEntity> specification);
}
