package com.artiexh.model.rest.user;

import com.artiexh.model.domain.UserAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressRequest {

	@JsonIgnore
	private Long id;

	@NotEmpty
	private String address;

	@NotNull
	private UserAddress.Type type;

	private Boolean isDefault = false;

	@NotEmpty
	private String phone;

	@NotEmpty
	private String receiverName;

	@NotNull
	private Integer wardId;

}
