package com.artiexh.model.rest.address;

import com.artiexh.model.domain.Ward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
	private Ward ward;
	private String address;
}
