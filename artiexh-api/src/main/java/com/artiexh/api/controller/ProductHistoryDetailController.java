package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.productinventory.ProductHistoryService;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.producthistory.ProductHistoryDetailFilter;
import com.artiexh.model.rest.producthistory.ProductHistoryDetailPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.ProductHistoryDetail.ROOT)
public class ProductHistoryDetailController {
	private final ProductHistoryService productHistoryService;
	@GetMapping()
	public PageResponse<ProductHistoryDetailPageResponse> getDetailInPage(
		@ParameterObject @Valid PaginationAndSortingRequest pagination,
		@ParameterObject ProductHistoryDetailFilter filter
	) {
//		Page<ProductHistory> productHistoryPage =
		Page<ProductHistoryDetailPageResponse> result = productHistoryService.getDetailInPage(pagination.getPageable(), filter.getSpecification());
		return new PageResponse<>(result);
	}
}
