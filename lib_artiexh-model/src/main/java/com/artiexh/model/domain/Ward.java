package com.artiexh.model.domain;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {
	@JsonSerialize(using = ToStringSerializer.class)
	private Integer id;

	private String name;

	private String fullName;

	private District district;
}
