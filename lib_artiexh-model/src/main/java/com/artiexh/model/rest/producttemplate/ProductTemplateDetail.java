package com.artiexh.model.rest.producttemplate;

import com.artiexh.model.rest.provider.ProviderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTemplateDetail extends ProductTemplateInfo {

	@Size(max = 1000)
	private String description;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Set<ProviderDetail> providers;
}
