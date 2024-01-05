package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CustomProductTagId.class)
@Table(name = "custom_product_tag")
public class CustomProductTagEntity {

	@Id
	@Column(name = "custom_product_id", nullable = false)
	private Long customProductId;

	@Id
	@Column(name = "name", nullable = false)
	private String name;

}