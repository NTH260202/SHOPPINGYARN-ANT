package com.artiexh.model.rest.campaign.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignProviderResponse {
	private String businessCode;
	private String businessName;
	private String address;
	private String contactName;
	private String email;
	private String phone;
	private String imageUrl;
	private String description;
	private String website;
	private Set<CustomProduct> customProducts;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CustomProduct {
		@JsonSerialize(using = ToStringSerializer.class)
		private Long id;
		private String name;
		private ProviderConfigResponse config;
	}

}
