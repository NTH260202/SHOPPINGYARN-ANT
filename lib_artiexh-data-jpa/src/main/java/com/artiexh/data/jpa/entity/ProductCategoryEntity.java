package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_category")
public class ProductCategoryEntity {

	@OneToMany(mappedBy = "category")
	private Set<ProductInventoryEntity> product;
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;
}