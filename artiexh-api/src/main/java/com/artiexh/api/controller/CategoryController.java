package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.CategoryService;
import com.artiexh.model.mapper.ProductCategoryMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.category.ProductCategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.Category.ROOT)
public class CategoryController {
	private final CategoryService categoryService;
	private final ProductCategoryMapper productCategoryMapper;

	@GetMapping
	public PageResponse<ProductCategoryResponse> getInPage(
		@RequestParam(required = false) String name,
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest
	) {
		Page<ProductCategoryResponse> categoryPage = categoryService.getInPage(
			name,
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(categoryPage);
	}
}
