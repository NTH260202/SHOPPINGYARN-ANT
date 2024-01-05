package com.artiexh.ghtk.client.service;

import com.artiexh.ghtk.client.model.GhtkResponse;
import com.artiexh.ghtk.client.model.order.CreateOrderRequest;
import com.artiexh.ghtk.client.model.order.CreateOrderResponse;
import com.artiexh.ghtk.client.model.order.OrderStatusResponse;
import com.artiexh.ghtk.client.model.shipfee.ShipFeeRequest;
import com.artiexh.ghtk.client.model.shipfee.ShipFeeResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface GhtkOrderService {

	@GetExchange("/services/shipment/fee")
	Mono<ShipFeeResponse> getShipFee(@RequestBody ShipFeeRequest request);

	@PostExchange("/services/shipment/order")
	Mono<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest, @RequestParam String ver);

	@GetExchange("/services/shipment/v2/{orderId}")
	Mono<OrderStatusResponse> getOrderStatus(@PathVariable String orderId);

	@PostExchange("/services/shipment/cancel/{orderId}")
	Mono<GhtkResponse> cancelOrder(@PathVariable String orderId);

}
