package com.artiexh.model.rest.product.request;

import com.artiexh.model.domain.ProductHistoryAction;
import com.artiexh.model.domain.ProductInventoryQuantity;
import com.artiexh.model.domain.SourceCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductQuantitiesRequest {
	@NotEmpty
	@Valid
	private Set<ProductInventoryQuantity> productQuantities;
	@NotNull
	private ProductHistoryAction action;
	private Long sourceId;
	private String sourceName;
	@NotNull
	private SourceCategory sourceCategory;
}
