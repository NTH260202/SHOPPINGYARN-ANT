package com.artiexh.model.rest.producthistory.producthistory;

import com.artiexh.model.domain.ProductHistoryAction;
import com.artiexh.model.domain.SourceCategory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProductHistoryPageResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private Instant createdDate;
	private ProductHistoryAction action;
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceId;
	private SourceCategory sourceCategory;
	private String sourceName;
}
