package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Collection {
	private Long id;
	private String name;
	private String imageUrl;
	private Long artistId;
}
