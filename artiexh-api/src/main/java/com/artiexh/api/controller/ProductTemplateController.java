package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.ProductTemplateService;
import com.artiexh.model.domain.ProductTemplate;
import com.artiexh.model.mapper.ProductTemplateMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.producttemplate.ProductTemplateDetail;
import com.artiexh.model.rest.producttemplate.ProductTemplateFilter;
import com.artiexh.model.rest.producttemplate.ProductTemplateInfo;
import com.artiexh.model.rest.producttemplate.request.UpdateProductTemplateDetail;
import com.artiexh.model.rest.producttemplate.request.UpdateProviderConfig;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.ProductTemplate.ROOT)
public class ProductTemplateController {
	private final ProductTemplateService productTemplateService;
	private final ProductTemplateMapper mapper;

	//Create Product Base
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProductTemplateDetail create(@Valid @RequestBody ProductTemplateDetail detail) {
		try {
			ProductTemplate productTemplate = mapper.detailToDomain(detail);
			productTemplate = productTemplateService.create(productTemplate);
			return mapper.domainToDetail(productTemplate);
		} catch (IllegalArgumentException | EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@PutMapping(Endpoint.ProductTemplate.DETAIL)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProductTemplateDetail update(
		@PathVariable Long id,
		@Valid @RequestBody UpdateProductTemplateDetail detail) {
		try {
			ProductTemplate productTemplate = mapper.detailToDomain(detail);
			productTemplate.setId(id);
			productTemplate = productTemplateService.update(productTemplate);
			return mapper.domainToDetail(productTemplate);
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		} catch (EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
	}

	@PutMapping(Endpoint.ProductTemplate.DETAIL + "/provider")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProductTemplateDetail updateProviderConfig(
		@PathVariable Long id,
		@Valid @RequestBody UpdateProviderConfig detail) {
		try {
			ProductTemplate productTemplate = mapper.detailToDomain(detail);
			productTemplate.setId(id);
			productTemplate = productTemplateService.updateProductTemplateConfig(productTemplate);
			return mapper.domainToDetail(productTemplate);
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		} catch (EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
		}
	}

	//Get Product Base Page

	@GetMapping
	public PageResponse<ProductTemplateInfo> getInPage(
		@ParameterObject ProductTemplateFilter filter,
		@Valid @ParameterObject PaginationAndSortingRequest pagination) {
		Page<ProductTemplate> productPage = productTemplateService.getInPage(filter.getSpecification(), pagination.getPageable());
		return new PageResponse<>(productPage.map(mapper::domainToInfo));
	}

	@GetMapping(Endpoint.ProductTemplate.DETAIL)
	public ProductTemplateDetail getById(@PathVariable("id") Long id) {
		try {
			ProductTemplate productTemplate = productTemplateService.getById(id);
			return mapper.domainToDetail(productTemplate);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.TEMPLATE_NOT_FOUND);
		}

	}

	@DeleteMapping(Endpoint.ProductTemplate.DETAIL)
	public void delete(@PathVariable("id") Long id) {
		try {
			productTemplateService.delete(id);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.TEMPLATE_NOT_FOUND);
		}

	}
}
