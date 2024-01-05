package com.artiexh.data.jpa.entity.embededmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageConfig {
	private String code = UUID.randomUUID().toString();
	private String name;
	private Double[] position;
	private Double[] rotate;
	private Double[] scale;
	private Size size;
	private Size mockupImageSize;
}
