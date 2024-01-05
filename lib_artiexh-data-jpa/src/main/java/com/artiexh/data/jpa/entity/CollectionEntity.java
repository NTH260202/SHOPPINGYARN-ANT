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
@Table(name = "collection")
public class CollectionEntity {
	@Id
	@Tsid
	private Long id;
	private String name;
	@Column(name = "image_url")
	private String imageUrl;
	@Column(name = "artist_id")
	private Long artistId;
}
