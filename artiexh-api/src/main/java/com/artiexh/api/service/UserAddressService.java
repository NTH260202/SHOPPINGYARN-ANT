package com.artiexh.api.service;

import com.artiexh.model.domain.UserAddress;
import com.artiexh.model.rest.user.UserAddressRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAddressService {
	Page<UserAddress> getByUserId(Long userId, Pageable pageable);

	UserAddress getById(Long userId, Long id);

	UserAddress create(Long userId, UserAddressRequest userAddressRequest);

	UserAddress update(Long userId, UserAddressRequest userAddressRequest);

	UserAddress delete(Long userId, Long id);
}
