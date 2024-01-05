package com.artiexh.model.rest.producthistory.producthistory;

import com.artiexh.model.domain.ProductSource;
import com.artiexh.model.rest.producthistory.ProductHistoryDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ProductHistoryResponse extends ProductHistoryPageResponse {
	private Set<ProductHistoryDetail> productHistoryDetails;
	private ProductSource productSource;
}
