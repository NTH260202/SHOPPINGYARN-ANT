package com.artiexh.model.rest.account;

import com.artiexh.model.domain.UserStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountStatusRequest {
	@NotNull
	private UserStatus status;
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long accountId;
}
