package com.artiexh.data.jpa.entity.embededmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
	private String name;
	private String code;
	private List<ImageConfig> images;
}
