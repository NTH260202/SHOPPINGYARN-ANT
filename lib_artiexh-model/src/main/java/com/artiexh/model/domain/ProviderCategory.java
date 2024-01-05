package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderCategory {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	@NotBlank
	private String name;

	private String imageUrl;

}
