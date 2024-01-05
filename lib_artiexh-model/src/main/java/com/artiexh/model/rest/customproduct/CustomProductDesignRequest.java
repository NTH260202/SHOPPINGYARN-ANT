package com.artiexh.model.rest.customproduct;

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
public class CustomProductDesignRequest {

	@JsonIgnore
	private Long id;

	@NotNull
	private Long variantId;

	@NotBlank
	@Size(max = 50)
	private String name;

	@JsonIgnore
	private Long artistId;

	@Valid
	private Set<ImageSet> imageSet = Set.of();

	private String combinationCode;

	private Long modelThumbnailId;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ImageSet {
		@NotNull
		private String positionCode;
		private Long mockupImageId;
		private Long manufacturingImageId;
	}
}
