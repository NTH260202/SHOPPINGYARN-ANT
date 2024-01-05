package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.ghtk.client.model.ShipmentRequest;
import com.artiexh.ghtk.client.model.shipfee.ShipFeeResponse;
import com.artiexh.model.domain.CampaignOrder;
import com.artiexh.model.rest.order.admin.response.AdminCampaignOrderResponse;
import com.artiexh.model.rest.order.request.GetShippingFeeRequest;
import com.artiexh.model.rest.order.request.UpdateShippingOrderRequest;
import com.artiexh.model.rest.order.user.response.AdminCampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.CampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import reactor.core.publisher.Mono;

public interface CampaignOrderService {

	Page<CampaignOrderResponsePage> getCampaignOrderInPage(Specification<CampaignOrderEntity> query, Pageable pageable);

	CampaignOrder getCampaignOrderById(Long orderId);

	Page<AdminCampaignOrderResponsePage> getAdminCampaignOrderInPage(Specification<CampaignOrderEntity> query, Pageable pageable);

	AdminCampaignOrderResponse getAdminCampaignOrderById(Long orderId);

	UserCampaignOrderDetailResponse getOrderByIdAndOwner(Long orderId, Long artistId);

	UserCampaignOrderDetailResponse getOrderByIdAndUserId(Long orderId, Long userId);

	Mono<ShipFeeResponse.ShipFee> getShippingFee(Long userId, GetShippingFeeRequest request);

	AdminCampaignOrderResponse updateShippingOrderStatus(Long orderId,
														 UpdateShippingOrderRequest updateShippingOrderRequest);

	void cancelOrder(Long orderId, String message, Long updatedBy);

	void refundOrder(Long orderId, Long createdBy);

	void updateShipment(ShipmentRequest request);

}
