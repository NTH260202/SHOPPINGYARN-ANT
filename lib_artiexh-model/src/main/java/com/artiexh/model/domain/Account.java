package com.artiexh.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Account {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String username;
	@JsonIgnore
	private String password;
	private String displayName;
	private Role role;
	private UserStatus status;
	private String avatarUrl;
	private String email;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Account account = (Account) o;
		return Objects.equals(id, account.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
