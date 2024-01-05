package com.artiexh.model.rest.order.admin.response;

import com.artiexh.model.domain.PaymentMethod;
import com.artiexh.model.rest.transaction.OrderTransactionResponse;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private PaymentMethod paymentMethod;
	private OrderTransactionResponse currentTransaction;
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
	private Owner owner;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Owner {
		@JsonSerialize(using = ToStringSerializer.class)
		private String id;
		private String username;
		private String displayName;
		private String avatarUrl;
	}
}
