package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image_set")
public class ImageSetEntity {
	@Id
	@Tsid
	private Long id;
	@Column(name = "position_code")
	private String positionCode;

	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "manufacturing_image_id", referencedColumnName = "id")
	private MediaEntity manufacturingImage;

	@OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "mockup_image_id", referencedColumnName = "id")
	private MediaEntity mockupImage;

}
