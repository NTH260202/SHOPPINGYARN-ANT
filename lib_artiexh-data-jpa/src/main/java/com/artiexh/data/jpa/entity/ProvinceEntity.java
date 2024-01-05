package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "province")
public class ProvinceEntity {

	@Id
	@Column(name = "id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "country_id", nullable = false)
	private CountryEntity country;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@OneToMany(mappedBy = "province")
	private Set<DistrictEntity> districts = new LinkedHashSet<>();

}