package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.data.jpa.entity.OrderEntity;
import com.artiexh.model.rest.order.user.response.CampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.DetailUserOrderResponse;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderDetailResponse;
import com.artiexh.model.rest.order.user.response.UserOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

	DetailUserOrderResponse getOrderById(Long id, Long userId);

	Page<UserOrderResponse> getOrderInPage(Specification<OrderEntity> specification, Pageable pageable);

	Page<CampaignOrderResponsePage> getCampaignOrderInPage(Specification<CampaignOrderEntity> specification,
														   Pageable pageable);

	UserCampaignOrderDetailResponse getCampaignOrderById(Long id, Long userId);

}
