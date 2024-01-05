package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_attach")
public class ProductAttachEntity {

	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "url", nullable = false, length = 2048)
	private String url;

	@Column(name = "type", nullable = false)
	private Byte type;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

}