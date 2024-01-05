package com.artiexh.api.controller.marketplace;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.marketplace.salecampaign.filter.ProductPageFilter;
import com.artiexh.model.rest.marketplace.salecampaign.filter.SaleCampaignFilter;
import com.artiexh.model.rest.marketplace.salecampaign.request.ProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.SaleCampaignRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.UpdateProductInSaleRequest;
import com.artiexh.model.rest.marketplace.salecampaign.request.UpdateSaleCampaignStatusRequest;
import com.artiexh.model.rest.marketplace.salecampaign.response.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = Endpoint.SaleCampaign.ROOT)
public class SaleCampaignController {
	private final SaleCampaignService saleCampaignService;
	private final ProductService productService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public SaleCampaignDetailResponse createSaleCampaign(Authentication authentication,
														 @RequestBody @Validated SaleCampaignRequest request) {
		Long id = (Long) authentication.getPrincipal();
		try {
			return saleCampaignService.createSaleCampaign(id, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public SaleCampaignDetailResponse updateSaleCampaign(@PathVariable("id") Long id,
														 @RequestBody @Validated SaleCampaignRequest request) {
		try {
			return saleCampaignService.updateSaleCampaign(id, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public void updateStatus(@PathVariable("id") Long id,
							 @RequestBody @Validated UpdateSaleCampaignStatusRequest request) {
		try {
			saleCampaignService.updateStatus(id, request.getStatus());
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PostMapping("/{id}/product-in-sale")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public Set<ProductResponse> createProductInSale(@PathVariable Long id,
													@RequestBody @Validated Set<ProductInSaleRequest> requests) {
		try {
			return saleCampaignService.createProductInSaleCampaign(id, requests);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PutMapping("/{id}/product-in-sale/{productCode}")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public ProductResponse updateProductInSale(@PathVariable Long id,
											   @PathVariable String productCode,
											   @RequestBody @Validated UpdateProductInSaleRequest request) {
		try {
			return saleCampaignService.updateProductInSaleCampaign(id, productCode, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@DeleteMapping("/{id}/product-in-sale")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public void deleteProductInSale(@PathVariable("id") Long campaignId,
									@RequestBody @Validated Set<String> productCodes) {
		try {
			saleCampaignService.deleteProductInSaleCampaign(campaignId, productCodes);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.PRODUCT_IN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/{id}/product-in-sale")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF','ARTIST')")
	public PageResponse<ProductResponse> getAllProductInSaleByCampaign(@PathVariable Long id,
																	   @ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																	   @ParameterObject ProductPageFilter filter) {
		filter.setCampaignId(id);
		return new PageResponse<>(productService.getAllProductResponse(
			paginationAndSortingRequest.getPageable(),
			filter.getQuery()
		));
	}

	@GetMapping("/{id}/statistics")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF','ARTIST')")
	public CampaignStatistics getStatistic(
		@PathVariable("id") Long id
	) {
		try {
			return saleCampaignService.getStatistics(id);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/{id}/sold-product")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF','ARTIST')")
	public PageResponse<ProductStatisticResponse> getSoldProduct(
		@PathVariable("id") Long id,
		@ParameterObject @Valid PaginationAndSortingRequest pagination
	) {
		try {
			Page<ProductStatisticResponse> page = saleCampaignService.getProductStatistic(id, pagination.getPageable());
			return new PageResponse<>(page);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF','ARTIST')")
	public SaleCampaignDetailResponse getDetail(
		@PathVariable("id") Long id
	) {
		try {
			return saleCampaignService.getDetail(id);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_SALE_NOT_FOUND);
		}
	}

	@GetMapping()
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF','ARTIST')")
	public PageResponse<SaleCampaignResponse> getAllSaleCampaign(@ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																 @ParameterObject SaleCampaignFilter filter) {
		return new PageResponse<>(saleCampaignService.getAll(
			paginationAndSortingRequest.getPageable(),
			filter.getSpecification())
		);
	}
}
