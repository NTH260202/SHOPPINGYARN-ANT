package com.artiexh.model.rest.customproduct;

import com.artiexh.model.domain.ProductAttach;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomProductGeneralRequest {
	@JsonIgnore
	private Long id;

	@NotNull
	private Long variantId;

	@NotBlank
	@Size(max = 50)
	private String name;

	@JsonIgnore
	private Long artistId;

	private String description;

	private Set<String> tags = Set.of();

	private Integer maxItemPerOrder;

	@Valid
	private Set<ProductAttach> attaches = Set.of();
}
