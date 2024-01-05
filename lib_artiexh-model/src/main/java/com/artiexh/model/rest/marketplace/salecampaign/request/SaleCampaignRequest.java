package com.artiexh.model.rest.marketplace.salecampaign.request;

import com.artiexh.model.domain.CampaignType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleCampaignRequest {
	@NotBlank
	private String name;

	private String description;

	@NotNull
	private Instant publicDate;

	@NotNull
	private Instant from;

	@NotNull
	private Instant to;

	private String content;

	private String thumbnailUrl;

	@NotNull
	private CampaignType type;

	@NotNull
	private Long artistId;

}
