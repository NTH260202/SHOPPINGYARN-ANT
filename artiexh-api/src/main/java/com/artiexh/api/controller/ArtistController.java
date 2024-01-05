package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.ArtistService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.api.service.marketplace.SaleCampaignService;
import com.artiexh.model.domain.Post;
import com.artiexh.model.mapper.PostMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.artist.request.UpdateArtistProfileRequest;
import com.artiexh.model.rest.artist.response.ArtistProfileResponse;
import com.artiexh.model.rest.marketplace.salecampaign.filter.ProductPageFilter;
import com.artiexh.model.rest.marketplace.salecampaign.filter.SaleCampaignFilter;
import com.artiexh.model.rest.marketplace.salecampaign.response.ProductMarketplaceResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignDetailResponse;
import com.artiexh.model.rest.marketplace.salecampaign.response.SaleCampaignResponse;
import com.artiexh.model.rest.order.request.CampaignOrderPageFilter;
import com.artiexh.model.rest.order.user.response.CampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderDetailResponse;
import com.artiexh.model.rest.post.PostDetail;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Artist.ROOT)
public class ArtistController {
	private final ArtistService artistService;
	private final ProductService productService;
	private final PostMapper postMapper;
	private final SaleCampaignService saleCampaignService;

	@GetMapping(Endpoint.Artist.ARTIST_PROFILE)
	public ArtistProfileResponse getProfile(@PathVariable String username) {
		try {
			return artistService.getProfile(username);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		}
	}

	@PutMapping("/profile")
	public ArtistProfileResponse updateProfile(
		Authentication authentication,
		@RequestBody @Valid UpdateArtistProfileRequest artistProfile) {
		try {
			Long id = (Long) authentication.getPrincipal();
			return artistService.updateArtistProfile(id, artistProfile);
		} catch (EntityNotFoundException ex) {
			throw new ResponseStatusException(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage(), ex);
		}
	}

	@GetMapping(Endpoint.Artist.ARTIST_PRODUCT)
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN', 'STAFF')")
	public PageResponse<ProductMarketplaceResponse> getAllProduct(
		Authentication authentication,
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid ProductPageFilter filter
	) {
		long userId = (long) authentication.getPrincipal();
		filter.setArtistId(userId);
		return new PageResponse<>(productService.getAllMarketplaceResponse(paginationAndSortingRequest.getPageable(), filter.getQuery()));
	}

	@GetMapping(Endpoint.Artist.ARTIST_ORDER)
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN', 'STAFF')")
	public PageResponse<CampaignOrderResponsePage> getAllOrder(
		Authentication authentication,
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid CampaignOrderPageFilter filter
	) {
		long userId = (long) authentication.getPrincipal();
		return artistService.getAllOrder(filter.getSpecificationForArtist(userId), paginationAndSortingRequest.getPageable());
	}

	@GetMapping(Endpoint.Artist.ARTIST_ORDER + "/{id}")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN', 'STAFF')")
	public UserCampaignOrderDetailResponse getOrderById(
		@PathVariable Long id,
		Authentication authentication
	) {
		try {
			long userId = (long) authentication.getPrincipal();
			return artistService.getOrderById(id, userId);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		}
	}

	@GetMapping(Endpoint.Artist.ARTIST_POST)
	public PageResponse<PostDetail> getArtistPost(@PathVariable long id,
												  @Valid @ParameterObject PaginationAndSortingRequest pagination) {
		try {
			Page<Post> posts = artistService.getArtistPost(id, pagination.getPageable());
			return new PageResponse<>(posts.map(postMapper::domainToDetail));
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.POST_NOT_FOUND);
		}
	}

	@GetMapping("/sale-campaign/{id}")
	@PreAuthorize("hasAnyAuthority('ARTIST')")
	public SaleCampaignDetailResponse getDetail(
		Authentication authentication,
		@PathVariable("id") Long id
	) {
		long userId = (long) authentication.getPrincipal();
		try {
			return saleCampaignService.getDetail(id, userId);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_SALE_NOT_FOUND);
		}
	}

	@GetMapping("/sale-campaign")
	public PageResponse<SaleCampaignResponse> getAllSaleCampaign(
		Authentication authentication,
		@ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject SaleCampaignFilter filter) {
		long userId = (long) authentication.getPrincipal();
		filter.setOwnerId(userId);
		return new PageResponse<>(saleCampaignService.getAll(
			paginationAndSortingRequest.getPageable(),
			filter.getSpecification())
		);
	}
}
