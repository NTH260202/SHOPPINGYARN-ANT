package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "media")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MediaEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "file_name", nullable = false, unique = true)
	private String fileName;

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "owner_id")
	private AccountEntity owner;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "account_media_mapping",
		joinColumns = @JoinColumn(
			name = "media_id", referencedColumnName = "id"
		),
		inverseJoinColumns = @JoinColumn(
			name = "shared_user_id", referencedColumnName = "id"
		)
	)
	private Set<AccountEntity> sharedUsers;
}
