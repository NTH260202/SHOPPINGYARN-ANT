package com.artiexh.model.rest.customproduct;

import com.artiexh.data.jpa.entity.embededmodel.ImageCombination;
import com.artiexh.data.jpa.entity.embededmodel.OptionConfig;
import com.artiexh.model.domain.Model3DCode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTemplateInCustomProductResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String code;
	private String name;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long modelFileId;
	private List<OptionConfig> sizes;
	private List<ImageCombination> imageCombinations;
	private Model3DCode model3DCode;
}
