package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "provider_category")
public class ProviderCategoryEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Size(max = 255)
	@Column(name = "name")
	private String name;

	@Size(max = 2048)
	@Column(name = "image_url", length = 2048)
	private String imageUrl;

}