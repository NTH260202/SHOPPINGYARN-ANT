package com.artiexh.model.rest.artist.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateArtistProfileRequest {
	@NotEmpty
	private String bankAccount;

	@NotEmpty
	private String bankName;

	@NotEmpty
	private String phone;

	private String shopThumbnailUrl;

	@Size(max = 1000)
	private String description;

	private Object metaData;

	@NotEmpty
	private String bankAccountName;
}
