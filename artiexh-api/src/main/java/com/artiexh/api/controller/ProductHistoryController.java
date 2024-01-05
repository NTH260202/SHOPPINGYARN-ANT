package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.productinventory.ProductHistoryService;
import com.artiexh.model.domain.ProductHistory;
import com.artiexh.model.mapper.ProductHistoryMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.producthistory.producthistory.ProductHistoryFilter;
import com.artiexh.model.rest.producthistory.producthistory.ProductHistoryPageResponse;
import com.artiexh.model.rest.producthistory.producthistory.ProductHistoryResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.ProductHistory.ROOT)
public class ProductHistoryController {
	private final ProductHistoryService productHistoryService;
	private final ProductHistoryMapper productHistoryMapper;

	@GetMapping
	public PageResponse<ProductHistoryPageResponse> getInPage(
		@ParameterObject @Valid PaginationAndSortingRequest pagination,
		@ParameterObject ProductHistoryFilter filter
	) {
		Page<ProductHistory> productHistoryPage = productHistoryService.getInPage(pagination.getPageable(), filter.getSpecification());
		return new PageResponse<>(productHistoryPage.map(productHistoryMapper::domainToPageResponse));
	}

	@GetMapping("{id}")
	public ProductHistoryResponse getById(
		@PathVariable("id") Long id) {
		try {
			ProductHistory productHistory = productHistoryService.getById(id);
			return productHistoryMapper.domainToResponse(productHistory);
		} catch (EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
	}
}
