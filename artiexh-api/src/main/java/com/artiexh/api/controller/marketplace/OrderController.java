package com.artiexh.api.controller.marketplace;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ArtiexhConfigException;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.CampaignOrderService;
import com.artiexh.api.service.OrderService;
import com.artiexh.ghtk.client.model.ShipmentRequest;
import com.artiexh.ghtk.client.model.shipfee.ShipFeeResponse;
import com.artiexh.model.rest.order.admin.response.AdminCampaignOrderResponse;
import com.artiexh.model.rest.order.request.*;
import com.artiexh.model.rest.order.response.PaymentResponse;
import com.artiexh.model.rest.order.user.response.DetailUserOrderResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Order.ROOT)
public class OrderController {
	private final OrderService orderService;
	private final CampaignOrderService campaignOrderService;

	@PostMapping(Endpoint.Order.CHECKOUT)
	@PreAuthorize("hasAnyAuthority('USER', 'ARTIST')")
	public DetailUserOrderResponse checkout(Authentication authentication,
											@RequestBody @Valid CheckoutRequest request) {
		var userId = (Long) authentication.getPrincipal();
		try {
			return orderService.checkout(userId, request);
		} catch (UnsupportedOperationException ex) {
			throw new InvalidException(ErrorCode.OPERATION_UNSUPPORTED, ex.getMessage());
		}
	}

	@GetMapping(Endpoint.Order.SHIPPING_FEE)
	@PreAuthorize("hasAnyAuthority('USER', 'ARTIST')")
	public ShipFeeResponse.ShipFee getShippingFee(Authentication authentication,
												  @ParameterObject @Valid GetShippingFeeRequest request) {
		var userId = (Long) authentication.getPrincipal();
		try {
			return campaignOrderService.getShippingFee(userId, request).block();
		} catch (ArtiexhConfigException ex) {
			throw new InvalidException(ErrorCode.ARTIEXH_CONFIG_ERROR, ex.getMessage());
		}
	}

	@GetMapping(Endpoint.Order.PAYMENT)
	public PaymentResponse payment(Authentication authentication,
								   HttpServletRequest request,
								   @PathVariable Long id,
								   @RequestParam String confirmUrl
	) {
		var userId = (Long) authentication.getPrincipal();
		try {
			String ip = request.getHeader("X-FORWARDED-FOR");

			if (ip == null || ip.isEmpty()) {
				ip = request.getRemoteAddr();
			}
			return PaymentResponse.builder()
				.paymentUrl(orderService.payment(
						id,
						PaymentQueryProperties.builder().vnp_IpAddr(ip).build(),
						userId,
						confirmUrl
					)
				)
				.build();
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		}
	}

	@GetMapping(Endpoint.Order.PAYMENT_RETURN)
	public ResponseEntity<Void> confirmUrl(@ParameterObject PaymentQueryProperties paymentQueryProperties) {
		var confirmUrl = orderService.confirmPayment(paymentQueryProperties);
		URI uri = URI.create(confirmUrl + "/" + paymentQueryProperties.getVnp_TxnRef());
		return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
	}

	@PatchMapping("/{id}/shipping")
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public AdminCampaignOrderResponse updateOrderToShippingStatus(
		@PathVariable Long id,
		@RequestBody @Valid UpdateShippingOrderRequest updateShippingOrderRequest
	) {
		try {
			return campaignOrderService.updateShippingOrderStatus(id, updateShippingOrderRequest);
		} catch (EntityNotFoundException exception) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		} catch (ArtiexhConfigException ex) {
			throw new InvalidException(ErrorCode.ARTIEXH_CONFIG_ERROR, ex.getMessage());
		}
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Void> updateStatus(
		Authentication authentication,
		@RequestBody @Valid UpdateOrderStatusRequest request,
		@PathVariable("id") Long id
	) {
		try {
			var userId = (Long) authentication.getPrincipal();
			switch (request.getStatus()) {
				case CANCELED -> campaignOrderService.cancelOrder(id, request.getMessage(), userId);
				case REFUNDING -> campaignOrderService.refundOrder(id, userId);
				default ->
					throw new InvalidException(ErrorCode.UPDATE_CAMPAIGN_ORDER_STATUS_FAILED, "Bạn chỉ có thể chuyển trạng thái sang CANCELED hoặc REFUNDING");
			}
			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.ORDER_NOT_FOUND);
		}
	}

	@PostMapping("/shipment")
	public void updateShipment(@ParameterObject ShipmentRequest request) {
		campaignOrderService.updateShipment(request);
	}

}
