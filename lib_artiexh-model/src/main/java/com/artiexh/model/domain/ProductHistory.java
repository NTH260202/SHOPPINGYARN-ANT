package com.artiexh.model.domain;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductHistory {
	private Long id;
	private Instant createdDate;
	private ProductHistoryAction action;
	private Long sourceId;
	private SourceCategory sourceCategory;
	private ProductSource productSource;
	private Set<ProductHistoryDetail> productHistoryDetails;
}
