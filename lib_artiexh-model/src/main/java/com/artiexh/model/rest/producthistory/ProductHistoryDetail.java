package com.artiexh.model.rest.producthistory;

import com.artiexh.model.domain.ProductInventoryQuantity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductHistoryDetail {
	private ProductInventoryQuantity productInventory;
	private Long quantity;
}
