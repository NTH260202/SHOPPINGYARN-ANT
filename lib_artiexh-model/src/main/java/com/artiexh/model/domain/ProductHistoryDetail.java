package com.artiexh.model.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductHistoryDetail {
	private ProductInventory productInventory;
	private Long quantity;
}
