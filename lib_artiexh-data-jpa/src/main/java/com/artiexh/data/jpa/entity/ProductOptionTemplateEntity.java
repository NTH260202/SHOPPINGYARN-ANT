package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "option_template")
public class ProductOptionTemplateEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "name", length = 50)
	private String name;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "option_id")
	private Set<OptionValueTemplateEntity> optionValues;
}
