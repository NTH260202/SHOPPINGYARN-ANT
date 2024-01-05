package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.service.productinventory.ProductInventoryJpaService;
import com.artiexh.model.domain.ProductInventory;
import com.artiexh.model.mapper.ProductInventoryMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.product.request.UpdateProductQuantitiesRequest;
import com.artiexh.model.rest.product.request.UpdateProductRequest;
import com.artiexh.model.rest.product.response.ProductResponse;
import com.artiexh.model.rest.productinventory.ProductInventoryFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = Endpoint.ProductInventory.ROOT)
public class ProductInventoryController {
	private final ProductInventoryJpaService productInventoryService;
	private final ProductInventoryMapper productInventoryMapper;

	@PutMapping(path = "{product-code}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ProductResponse update(
		Authentication authentication,
		@PathVariable("product-code") String productCode,
		@RequestBody @Valid UpdateProductRequest request) {
		long userId = (long) authentication.getPrincipal();

		ProductInventory product = productInventoryMapper.updateProductRequestToProduct(request);
		product.setProductCode(productCode);
		try {
			ProductInventory updatedProduct = productInventoryService.update(product);
			return productInventoryMapper.domainToProductResponse(updatedProduct);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@PutMapping()
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public void updateQuantities(
		Authentication authentication,
		@RequestBody @Valid UpdateProductQuantitiesRequest request) {
		try {
			productInventoryService.updateQuantities(request);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
		}
	}

	@GetMapping
	public PageResponse<ProductResponse> getInPage(
		@ParameterObject @Valid PaginationAndSortingRequest pagination,
		@ParameterObject ProductInventoryFilter filter
	) {
		Page<ProductInventory> product = productInventoryService.getInPage(filter.getSpecification(), pagination.getPageable());
		return new PageResponse<>(product.map(productInventoryMapper::domainToProductResponse));
	}

	@GetMapping("{product-code}")
	public ProductResponse getDetail(
		@PathVariable("product-code") String productCode
	) {
		try {
			ProductInventory product = productInventoryService.getDetail(productCode);
			return productInventoryMapper.domainToProductResponse(product);
		} catch (EntityNotFoundException exception) {
			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), exception);
		}
	}

	@DeleteMapping("{product-code}")
	public void delete(
		@PathVariable("product-code") String productCode
	) {
		try {
			productInventoryService.delete(productCode);
		} catch (EntityNotFoundException exception) {
			throw new ResponseStatusException(ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(), exception);
		}
	}

}
