package com.artiexh.model.rest.account;

import com.artiexh.model.domain.Role;
import com.artiexh.model.domain.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class AccountProfile {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String username;
	private String displayName;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Role role;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private UserStatus status;
	private String avatarUrl;
	private String email;
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long numOfSubscriptions;
}
