package com.artiexh.api.controller.marketplace;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.ArtistService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import com.artiexh.model.domain.ProductSuggestion;
import com.artiexh.model.mapper.ArtistMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.artist.request.ArtistFilter;
import com.artiexh.model.rest.artist.response.ArtistProfileResponse;
import com.artiexh.model.rest.marketplace.salecampaign.filter.MarketplaceSaleCampaignFilter;
import com.artiexh.model.rest.marketplace.salecampaign.filter.ProductPageFilter;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignDetailResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignResponse;
import com.artiexh.model.rest.product.request.SuggestionFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Marketplace.ROOT)
public class MarketplaceController {
	private final ArtistService artistService;
	private final ArtistMapper artistMapper;
	private final SaleCampaignService saleCampaignService;
	private final ProductService productService;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@GetMapping("/sale-campaign")
	public PageResponse<SaleCampaignResponse> getAllSaleCampaign(@ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																 @ParameterObject MarketplaceSaleCampaignFilter filter) {
		return new PageResponse<>(saleCampaignService.getAll(
			paginationAndSortingRequest.getPageable(),
			filter.getMarketplaceSpecification(false))
		);
	}

	@GetMapping("/sale-campaign/{id}")
	public SaleCampaignDetailResponse getSaleCampaignDetailResponse(@PathVariable Long id) {
		try {
			return saleCampaignService.getDetail(id);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/sale-campaign/{id}/product-in-sale")
	public PageResponse<ProductMarketplaceResponse> getAllProductInSaleByCampaign(@PathVariable Long id,
																				  @ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																				  @ParameterObject ProductPageFilter filter) {
		filter.setCampaignId(id);
		return new PageResponse<>(productService.getAllMarketplaceResponse(
			paginationAndSortingRequest.getPageable(),
			filter.getQuery()
		));
	}

	@GetMapping("/product-in-sale")
	public PageResponse<ProductMarketplaceResponse> getAllProductInSale(@ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																		@ParameterObject ProductPageFilter filter) {
		return new PageResponse<>(productService.getAllMarketplaceResponse(
			paginationAndSortingRequest.getPageable(),
			filter.getMarketplaceQuery(false))
		);
	}

	@GetMapping("/sale-campaign/{campaignId}/product-in-sale/{productCode}")
	public ProductMarketplaceResponse getProductInSaleDetail(@PathVariable Long campaignId, @PathVariable String productCode) {
		try {
			return productService.getByCampaignIdAndProductCode(campaignId, productCode);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.PRODUCT_IN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/artist/{username}/sale-campaign")
	public PageResponse<SaleCampaignResponse> getAllCampaignByArtist(@PathVariable String username,
																	 @ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																	 @ParameterObject MarketplaceSaleCampaignFilter filter) {
		try {
			return new PageResponse<>(
				saleCampaignService.getAllByArtist(username, paginationAndSortingRequest.getPageable(), filter)
			);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ARTIST_NOT_FOUND);
		}
	}

	@GetMapping("/artist/{username}/product-in-sale")
	public PageResponse<ProductMarketplaceResponse> getAllProductInSaleByArtist(@PathVariable String username,
																				@ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
																				@ParameterObject ProductPageFilter filter) {
		try {
			return new PageResponse<>(
				productService.getAllByArtist(username, paginationAndSortingRequest.getPageable(), filter)
			);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ARTIST_NOT_FOUND);
		}
	}


	@GetMapping("/product/suggestion")
	public PageResponse<ProductSuggestion> getSuggestionInPage(
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid SuggestionFilter filter
	) {
		Page<ProductSuggestion> suggestionPage = productService.getSuggestionInPage(
			filter.getQuery(),
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(suggestionPage);
	}

	@GetMapping("/artist")
	public PageResponse<ArtistProfileResponse> getInPage(
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid ArtistFilter filter
	) {
		Page<ArtistProfileResponse> profiles = artistService.getAllProfile(
			filter.getSpecification(),
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(profiles);
	}
}
