package com.artiexh.model.rest.customproduct;

import com.artiexh.model.domain.ProductAttach;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomProductGeneralResponse extends CustomProductResponse {
	private String description;
	private Integer maxItemPerOrder;
	private Set<ProductAttach> attaches;
}
