package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "option_value_template")
public class OptionValueTemplateEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "option_id")
	private Long optionId;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "value")
	private String value;
}
