package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.TagService;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.tag.ProductTagResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.Tag.ROOT)
public class TagController {
	private final TagService tagService;

	@GetMapping
	public PageResponse<ProductTagResponse> getInPage(
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest
	) {
		Page<ProductTagResponse> tagPage = tagService.getInPage(
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(tagPage);
	}
}
