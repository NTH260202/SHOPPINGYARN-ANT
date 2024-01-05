package com.artiexh.model.rest.category;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private String id;
	private String name;
	private String imageUrl;
}
