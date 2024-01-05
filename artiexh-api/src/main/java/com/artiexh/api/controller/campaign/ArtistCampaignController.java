package com.artiexh.api.controller.campaign;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.campaign.CampaignService;
import com.artiexh.api.service.provider.ProviderService;
import com.artiexh.model.domain.CampaignHistory;
import com.artiexh.model.domain.Role;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.campaign.request.ArtistCampaignRequest;
import com.artiexh.model.rest.campaign.request.CampaignRequestFilter;
import com.artiexh.model.rest.campaign.request.UpdateCampaignStatusRequest;
import com.artiexh.model.rest.campaign.response.CampaignDetailResponse;
import com.artiexh.model.rest.campaign.response.CampaignProviderResponse;
import com.artiexh.model.rest.campaign.response.CampaignResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static com.artiexh.model.domain.CampaignStatus.ALLOWED_ADMIN_VIEW_STATUS;

@RestController
@RequestMapping(Endpoint.ArtistCampaign.ROOT)
@RequiredArgsConstructor
public class ArtistCampaignController {
	private final CampaignService campaignService;
	private final ProviderService providerService;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public CampaignDetailResponse createCampaign(Authentication authentication,
												 @RequestBody @Validated ArtistCampaignRequest request) {
		var role = extractRole(authentication)
			.orElseThrow(() -> new InvalidException(ErrorCode.USER_NO_ROLE));
		long ownerId = role == Role.STAFF ? rootAdminId : (long) authentication.getPrincipal();

		return campaignService.createCampaign(ownerId, request);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public CampaignDetailResponse updateCampaign(Authentication authentication,
												 @PathVariable Long id,
												 @RequestBody @Validated ArtistCampaignRequest request) {
		var role = extractRole(authentication)
			.orElseThrow(() -> new InvalidException(ErrorCode.USER_NO_ROLE));
		long ownerId = role == Role.STAFF ? rootAdminId : (long) authentication.getPrincipal();

		request.setId(id);
		try {
			return campaignService.updateCampaign(ownerId, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		}
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public CampaignResponse updateStatus(Authentication authentication,
										 @PathVariable Long id,
										 @RequestBody @Validated UpdateCampaignStatusRequest request) {
		var role = extractRole(authentication)
			.orElseThrow(() -> new InvalidException(ErrorCode.USER_NO_ROLE));

		Long userId = (Long) authentication.getPrincipal();
		request.setId(id);

		try {
			return switch (role) {
				case ARTIST -> campaignService.artistUpdateStatus(userId, request);
				case ADMIN, STAFF -> campaignService.reviewCampaign(userId, request);
				default -> throw new InvalidException(ErrorCode.USER_NO_ROLE);
			};
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
		} catch (UnsupportedOperationException ex) {
			throw new InvalidException(ErrorCode.OPERATION_UNSUPPORTED, ex.getMessage());
		}

	}

	@GetMapping("/provider")
	@PreAuthorize("hasAuthority('ARTIST')")
	public Set<CampaignProviderResponse> getProviderSupportCustomProducts(Authentication authentication,
																		  @ParameterObject @RequestParam Set<Long> customProductIds) {
		long artistId = (long) authentication.getPrincipal();
		return providerService.getAllSupportedCustomProducts(artistId, customProductIds);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public PageResponse<CampaignResponse> getAllCampaigns(Authentication authentication,
														  @ParameterObject @Validated PaginationAndSortingRequest paginationAndSortingRequest,
														  @ParameterObject CampaignRequestFilter filter) {
		var role = extractRole(authentication)
			.orElseThrow(() -> new InvalidException(ErrorCode.USER_NO_ROLE));
		Long userId = (Long) authentication.getPrincipal();

		if (role == Role.ARTIST) {
			if (filter.getOwnerId() != null && !filter.getOwnerId().equals(userId.toString())) {
				return new PageResponse<>();
			} else {
				filter.setOwnerId(userId.toString());
			}
		} else { // ADMIN and STAFF
			if (filter.getStatus() == null || filter.getStatus().isEmpty()) {
				filter.setStatus(ALLOWED_ADMIN_VIEW_STATUS);
			} else if (!ALLOWED_ADMIN_VIEW_STATUS.containsAll(filter.getStatus())) {
				return new PageResponse<>();
			}
		}

		var response = campaignService.getAllCampaigns(filter.getSpecification(), paginationAndSortingRequest.getPageable());
		return new PageResponse<>(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ARTIST','ADMIN','STAFF')")
	public CampaignDetailResponse getCampaign(Authentication authentication,
											  @PathVariable Long id) {
		var role = extractRole(authentication)
			.orElseThrow(() -> new InvalidException(ErrorCode.USER_NO_ROLE));
		long userId = role == Role.STAFF ? rootAdminId : (long) authentication.getPrincipal();

		try {
			return campaignService.getCampaignDetail(userId, id);
		} catch (UsernameNotFoundException ex) {
			throw new InvalidException(ErrorCode.USER_NO_USERNAME);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.CAMPAIGN_REQUEST_NOT_FOUND);
		}
	}

	@GetMapping("/{id}/campaign-history")
	public PageResponse<CampaignHistory> getCampaignHistory(
		@PathVariable Long id,
		@ParameterObject @Valid PaginationAndSortingRequest pagination
	) {
		pagination.setSortBy("id.eventTime");
		return new PageResponse<>(campaignService.getCampaignHistory(id, pagination.getPageable()));
	}

	private Optional<Role> extractRole(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.findFirst()
			.map(Role::valueOf);
	}
}
