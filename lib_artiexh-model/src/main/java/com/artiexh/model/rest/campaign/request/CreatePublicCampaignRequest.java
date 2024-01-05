package com.artiexh.model.rest.campaign.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePublicCampaignRequest {
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long artistId;
	@NotBlank
	private String name;
}
