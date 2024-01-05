package com.artiexh.model.rest.option;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionValueDetail {
	private Long id;

	private String name;

	private String value;
}
