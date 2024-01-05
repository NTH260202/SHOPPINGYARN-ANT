package com.artiexh.model.rest.order.user.response;

import com.artiexh.model.rest.campaign.response.CampaignResponse;
import com.artiexh.model.rest.order.response.OrderDetailResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminCampaignOrderResponsePage extends CampaignOrderResponsePage {
	private CampaignResponse.Owner user;
	private Set<OrderDetailResponse> orderDetails;
	private BigDecimal totalPrice;
	private ShippingResponse shipment;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ShippingResponse {
		private String deliveryAddress;
		private String deliveryWard;
		private String deliveryDistrict;
		private String deliveryProvince;
		private String deliveryCountry;
		private String deliveryTel;
		private String deliveryEmail;
		private String deliveryName;
		private String pickAddress;
		private String pickWard;
		private String pickDistrict;
		private String pickProvince;
		private String pickCountry;
		private String pickTel;
		private String pickName;
		private String pickEmail;
		private String returnAddress;
		private String returnWard;
		private String returnDistrict;
		private String returnProvince;
		private String returnCountry;
		private String returnTel;
		private String returnName;
		private String returnEmail;
	}
}
