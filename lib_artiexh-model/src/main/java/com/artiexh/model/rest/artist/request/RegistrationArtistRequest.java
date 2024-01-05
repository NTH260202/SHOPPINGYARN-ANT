package com.artiexh.model.rest.artist.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationArtistRequest {

	@NotEmpty
	private String bankAccount;

	@NotEmpty
	private String bankAccountName;

	@NotEmpty
	private String bankName;

	@NotEmpty
	private String phone;

	private String shopThumbnailUrl;

	@Size(max = 1000)
	private String description;

	private Object metaData;
}