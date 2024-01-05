package com.artiexh.model.rest.address;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Short id;
	private String name;
	@JsonSerialize(using = ToStringSerializer.class)
	private Short countryId;
	private String countryName;
	private String fullAddress;
}
