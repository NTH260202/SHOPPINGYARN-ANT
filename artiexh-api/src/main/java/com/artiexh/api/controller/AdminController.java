package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.CampaignOrderService;
import com.artiexh.api.service.OrderService;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.order.admin.response.AdminCampaignOrderResponse;
import com.artiexh.model.rest.order.admin.response.AdminOrderResponse;
import com.artiexh.model.rest.order.admin.response.DetailAdminOrderResponse;
import com.artiexh.model.rest.order.request.CampaignOrderPageFilter;
import com.artiexh.model.rest.order.request.OrderPageFilter;
import com.artiexh.model.rest.order.user.response.AdminCampaignOrderResponsePage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Admin.ROOT)
public class AdminController {
	private final CampaignOrderService campaignOrderService;
	private final OrderService orderService;

	@GetMapping(Endpoint.Admin.CAMPAIGN_ORDER)
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public PageResponse<AdminCampaignOrderResponsePage> getAllOrder(
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid CampaignOrderPageFilter filter
	) {
		var campaignOrderPage = campaignOrderService.getAdminCampaignOrderInPage(
			filter.getSpecification(),
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(campaignOrderPage);
	}

	@GetMapping(Endpoint.Admin.CAMPAIGN_ORDER + "/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public AdminCampaignOrderResponse getCampaignOrderById(@PathVariable Long id) {
		try {
			return campaignOrderService.getAdminCampaignOrderById(id);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		}
	}

	@GetMapping(Endpoint.Admin.ORDER)
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public PageResponse<AdminOrderResponse> getAllOrder(
		@ParameterObject @Valid PaginationAndSortingRequest paginationAndSortingRequest,
		@ParameterObject @Valid OrderPageFilter filter
	) {
		var campaignOrderPage = orderService.getAllOrder(
			filter.getSpecification(),
			paginationAndSortingRequest.getPageable()
		);
		return new PageResponse<>(campaignOrderPage);
	}

	@GetMapping(Endpoint.Admin.ORDER + "/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public DetailAdminOrderResponse getOrderById(
		@PathVariable Long id
	) {
		try {
			return orderService.getById(id);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		}
	}

}
