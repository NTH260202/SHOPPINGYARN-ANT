package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountEntity {

	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", length = 60)
	private String password;

	@Column(name = "display_name", nullable = false)
	private String displayName;

	@Column(name = "role", nullable = false)
	private Byte role;

	@Column(name = "status", nullable = false)
	private Byte status;

	@Column(name = "email", length = 254)
	private String email;

	@Column(name = "avatar_url", length = 2048)
	private String avatarUrl;

}