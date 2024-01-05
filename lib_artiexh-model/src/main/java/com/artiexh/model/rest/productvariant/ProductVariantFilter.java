package com.artiexh.model.rest.productvariant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantFilter {
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long productTemplateId;

	@JsonSerialize(using = StringCollectionSerializer.class)
	private Set<Long> optionValueIds;
}
