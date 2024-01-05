package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig {
	@Size(max = 25)
	private String key;
	private String value;
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String updatedBy;
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private Instant modifiedDate;
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private Instant createdDate;
}
