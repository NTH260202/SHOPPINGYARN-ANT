package com.artiexh.data.jpa.entity;

import com.artiexh.data.jpa.entity.embededmodel.ProductHistoryEntityDetailId;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_history_detail")
public class ProductHistoryDetailEntity {
	@EmbeddedId
	private ProductHistoryEntityDetailId id;

	private Long quantity;

	@Column(name = "current_quantity")
	private Long currentQuantity;

	@ManyToOne
	@JoinColumn(name = "product_code", updatable = false, insertable = false)
	private ProductInventoryEntity productInventory;

	@ManyToOne
	@JoinColumn(name = "product_history_id", updatable = false, insertable = false)
	private ProductHistoryEntity productHistory;
}
