package com.artiexh.model.rest.option;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionDetail {
	private Long id;

	private Long productTemplateId;

	private String name;

	private Integer index;

	private boolean isOptional;

	private Set<OptionValueDetail> optionValues;
}
