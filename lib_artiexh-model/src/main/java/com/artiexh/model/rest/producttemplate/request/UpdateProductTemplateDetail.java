package com.artiexh.model.rest.producttemplate.request;

import com.artiexh.data.jpa.entity.embededmodel.ImageCombination;
import com.artiexh.data.jpa.entity.embededmodel.OptionConfig;
import com.artiexh.model.domain.ProductAttach;
import com.artiexh.model.domain.ProductOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductTemplateDetail {
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotBlank
	private String name;

	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long modelFileId;

	@NotNull
	@Valid
	private Set<ProductOption> productOptions;

	@NotEmpty
	private Set<ProductAttach> attaches;

	@NotNull
	private List<OptionConfig> sizes;

	private String sizeDescriptionUrl;

	@NotNull
	private List<ImageCombination> imageCombinations;

	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long categoryId;

	@Size(max = 1000)
	private String description;
}
