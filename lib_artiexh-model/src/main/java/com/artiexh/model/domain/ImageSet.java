package com.artiexh.model.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageSet {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	private Media mockupImage;

	private Media manufacturingImage;

	private String positionCode;
}
