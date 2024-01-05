package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.OrderEntity;
import com.artiexh.model.rest.order.admin.response.AdminOrderResponse;
import com.artiexh.model.rest.order.admin.response.DetailAdminOrderResponse;
import com.artiexh.model.rest.order.request.CheckoutRequest;
import com.artiexh.model.rest.order.request.PaymentQueryProperties;
import com.artiexh.model.rest.order.user.response.DetailUserOrderResponse;
import com.artiexh.model.rest.order.user.response.UserOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OrderService {

	DetailUserOrderResponse checkout(long userId, CheckoutRequest request);

	Page<UserOrderResponse> getUserOrderInPage(Specification<OrderEntity> query, Pageable pageable);

	DetailUserOrderResponse getUserDetailById(Long orderId);

	DetailUserOrderResponse getUserDetailByIdAndUserId(Long orderId, Long userId);

	Page<AdminOrderResponse> getAllOrder(Specification<OrderEntity> query, Pageable pagination);

	DetailAdminOrderResponse getById(Long id);

	String payment(Long id, PaymentQueryProperties paymentQueryProperties, Long userId, String confirmUrl);

	String confirmPayment(PaymentQueryProperties paymentQueryProperties);

	void cancelOrder(long orderId, long userId, String message);

	void closedTimeoutOrder();

}
