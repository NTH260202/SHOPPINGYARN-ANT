package com.artiexh.model.rest.customproduct;

import com.artiexh.model.domain.ImageSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CustomProductDesignResponse extends CustomProductResponse {
	private Set<ImageSet> imageSet;
	private String combinationCode;
}
