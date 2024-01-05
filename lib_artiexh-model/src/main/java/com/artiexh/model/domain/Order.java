package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	private Long id;
	private User user;
	private OrderTransaction currentTransaction;
	private Set<CampaignOrder> campaignOrders;
	private PaymentMethod paymentMethod;
	private String deliveryName;
	private String deliveryAddress;
	private String deliveryWard;
	private String deliveryDistrict;
	private String deliveryProvince;
	private String deliveryCountry;
	private String deliveryTel;
	private String deliveryEmail;
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
