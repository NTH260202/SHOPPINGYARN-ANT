package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.OptionService;
import com.artiexh.model.domain.ProductOption;
import com.artiexh.model.mapper.ProductOptionMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.option.OptionDetail;
import com.artiexh.model.rest.option.OptionFilter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Option.ROOT)
public class OptionController {
	private final OptionService optionService;
	private final ProductOptionMapper optionMapper;

	@GetMapping
	public PageResponse<OptionDetail> getAll(
		@ParameterObject PaginationAndSortingRequest request,
		@ParameterObject OptionFilter optionFilter
	) {
		Page<ProductOption> options = optionService.getAll(optionFilter.getSpecification(), request.getPageable());
		return new PageResponse<>(options.map(optionMapper::domainToDetail));
	}

	@GetMapping(Endpoint.Option.TEMPLATE)
	public PageResponse<OptionDetail> getAllTemplate(
		@ParameterObject PaginationAndSortingRequest request
	) {
		Page<ProductOption> options = optionService.getAllTemplate(null, request.getPageable());
		return new PageResponse<>(options.map(optionMapper::domainToDetail));
	}

	@GetMapping(Endpoint.Option.ACTIVE_OPTION)
	public Map<String, Set<String>> getActiveVariantOption(
		@RequestParam Long productTemplateId,
		@RequestParam(required = false) Long[] optionValueIds
	) {
		return optionService.getActiveVariantOption(productTemplateId, optionValueIds);
	}
}
