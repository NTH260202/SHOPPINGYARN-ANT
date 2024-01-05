package com.artiexh.model.rest.customproduct;

import com.artiexh.model.domain.Media;
import com.artiexh.model.domain.ProductCategory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomProductResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String name;
	private Long campaignLock;
	private Set<String> tags;
	private Media modelThumbnail;
	private ProductCategory category;
	private Instant createdDate;
	private Instant modifiedDate;
	private ProductVariant variant;
	private Boolean isDeleted;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ProductVariant {
		@JsonSerialize(using = ToStringSerializer.class)
		private Long id;
		private Set<ProductVariantCombinationResponse> variantCombinations;
		private ProductTemplateInCustomProductResponse productTemplate;
	}
}
