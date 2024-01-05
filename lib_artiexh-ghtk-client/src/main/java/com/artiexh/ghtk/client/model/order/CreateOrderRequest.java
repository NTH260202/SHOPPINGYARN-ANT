package com.artiexh.ghtk.client.model.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateOrderRequest {
	private Set<Product> products;
	private Order order;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class Order {
		private String id;
		private String pickName;
		private Integer pickMoney;
		private String pickAddressId;
		private String pickAddress;
		private String pickProvince;
		private String pickDistrict;
		private String pickWard;
		private String pickStreet;
		private String pickTel;
		private String pickEmail;
		private String name;
		private String address;
		private String province;
		private String district;
		private String ward;
		private String street;
		private String hamlet;
		private String tel;
		private String note;
		private String email;
		private Integer useReturnAddress;
		private String returnName;
		private String returnAddress;
		private String returnProvince;
		private String returnDistrict;
		private String returnWard;
		private String returnStreet;
		private String returnTel;
		private String returnEmail;
		private Integer isFreeship;
		private String weightOption;
		private Double totalWeight;
		private Integer pickWorkShift;
		private Integer deliverWorkShift;
		private String labelId;
		private String pickDate;
		private String deliverDate;
		private String expired;
		private Integer value;
		private Integer opm;
		private String pickOption;
		private String actualTransferMethod;
		private String transport;
		private String deliverOption;
		private String pickSession;
		private Set<Integer> tags;
	}
}
