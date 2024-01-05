package com.artiexh.model.rest.producthistory;

import com.artiexh.model.domain.ProductInventoryQuantity;
import com.artiexh.model.rest.producthistory.producthistory.ProductHistoryPageResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductHistoryDetailPageResponse {
	private ProductInventoryQuantity productInventory;
	private Long quantity;
	private Long remainingQuantity;
	private ProductHistoryPageResponse productHistory;
}
