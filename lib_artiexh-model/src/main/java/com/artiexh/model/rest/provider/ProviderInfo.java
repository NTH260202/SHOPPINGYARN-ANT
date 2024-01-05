package com.artiexh.model.rest.provider;

import com.artiexh.model.domain.ProviderCategory;
import com.artiexh.model.validation.BusinessCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderInfo {
	@BusinessCode
	@NotBlank
	private String businessCode;

	@NotBlank
	private String businessName;

	@NotBlank
	private String address;

	@NotBlank
	private String contactName;

	@NotBlank
	@Email
	private String email;

	@Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$",
		message = "Phone must be based on ITU-T standards. Ex: +84 382913108")
	private String phone;

	@NotBlank
	private String imageUrl;

	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<Long> categoryIds;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Set<ProviderCategory> categories;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Boolean isDeleted;
}
