package com.artiexh.model.rest.campaign.request;

import com.artiexh.model.domain.CampaignStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCampaignStatusRequest {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotNull
	private CampaignStatus status;

	private String message;

}
