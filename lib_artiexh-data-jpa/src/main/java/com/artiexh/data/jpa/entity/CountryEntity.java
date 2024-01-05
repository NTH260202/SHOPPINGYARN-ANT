package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "country")
public class CountryEntity {

	@Id
	@Column(name = "id", nullable = false)
	private Short id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "country")
	private Set<ProvinceEntity> provinces = new LinkedHashSet<>();

}