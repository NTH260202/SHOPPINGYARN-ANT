package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryQuantity {
	@NotBlank
	private String productCode;
	@NotNull
	private Long quantity;
	@JsonIgnore
	private Long currentQuantity;
}
