package com.artiexh.api.controller.campaign;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.campaign.CampaignService;
import com.artiexh.model.mapper.CampaignMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.campaign.request.CreatePublicCampaignRequest;
import com.artiexh.model.rest.campaign.request.PublicCampaignFilter;
import com.artiexh.model.rest.campaign.response.CampaignDetailResponse;
import com.artiexh.model.rest.campaign.response.CampaignResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.PublicCampaign.ROOT)
@RequiredArgsConstructor
public class PublicCampaignController {
	private final CampaignService campaignService;
	private final CampaignMapper campaignMapper;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('STAFF','ADMIN')")
	public CampaignDetailResponse createPublicCampaign(
		Authentication authentication,
		@RequestBody @Valid CreatePublicCampaignRequest request
	) {
		long createdBy = (long) authentication.getPrincipal();
		return campaignService.createPublicCampaign(createdBy, request);
	}

	@GetMapping
	@PreAuthorize("hasAnyAuthority('STAFF','ADMIN')")
	public PageResponse<CampaignResponse> getPublicCampaign(
		@ParameterObject @Valid PaginationAndSortingRequest pagination,
		@ParameterObject @Valid PublicCampaignFilter filter
	) {

		return new PageResponse<>(campaignService.getAllCampaigns(filter.getSpecification(), pagination.getPageable()));
	}

}
