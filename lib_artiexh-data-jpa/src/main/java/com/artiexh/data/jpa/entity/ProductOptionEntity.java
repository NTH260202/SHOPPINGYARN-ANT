package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_option")
public class ProductOptionEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "product_template_id")
	private Long productTemplateId;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "`index`")
	private Integer index;

	@Column(name = "is_optional")
	private Boolean isOptional;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "option_id")
	private Set<OptionValueEntity> optionValues;
}
