package com.artiexh.model.rest.order.user.response;

import com.artiexh.model.domain.PaymentMethod;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private PaymentMethod paymentMethod;
	private String deliveryAddress;
	private String deliveryWard;
	private String deliveryDistrict;
	private String deliveryProvince;
	private String deliveryCountry;
	private String deliveryTel;
	private String deliveryEmail;
	private String deliveryName;
	private Set<UserCampaignOrderResponse> campaignOrders;
}
