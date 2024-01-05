package com.artiexh.model.domain;

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
@Builder
public class Province {
	@JsonSerialize(using = ToStringSerializer.class)
	private Short id;
	private String name;
	private String fullName;
	private Country country;
	private Set<District> districts;
}
