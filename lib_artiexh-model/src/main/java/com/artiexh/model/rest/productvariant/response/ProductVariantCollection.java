package com.artiexh.model.rest.productvariant.response;

import com.artiexh.model.rest.productvariant.ProductVariantDetail;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantCollection {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long productTemplateId;

	private Set<ProductVariantDetail> variants;
}
