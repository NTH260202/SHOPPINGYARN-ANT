package com.artiexh.api.service.impl;

import com.artiexh.api.service.UserAddressService;
import com.artiexh.data.jpa.entity.UserAddressEntity;
import com.artiexh.data.jpa.entity.UserEntity;
import com.artiexh.data.jpa.repository.UserAddressRepository;
import com.artiexh.data.jpa.repository.WardRepository;
import com.artiexh.model.domain.UserAddress;
import com.artiexh.model.mapper.UserAddressMapper;
import com.artiexh.model.rest.user.UserAddressRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserAddressServiceImpl implements UserAddressService {
	private final UserAddressRepository userAddressRepository;
	private final WardRepository wardRepository;
	private final UserAddressMapper userAddressMapper;

	@Override
	public Page<UserAddress> getByUserId(Long userId, Pageable pageable) {
		return userAddressRepository.findByUserId(userId, pageable)
			.map(userAddressMapper::entityToDomain);
	}

	@Override
	public UserAddress getById(Long userId, Long id) {
		return userAddressRepository.findByIdAndUserId(id, userId)
			.map(userAddressMapper::entityToDomain)
			.orElseThrow(() -> new EntityNotFoundException("Address not existed"));
	}

	@Transactional
	@Override
	public UserAddress create(Long userId, UserAddressRequest userAddressRequest) {
		if (!wardRepository.existsById(userAddressRequest.getWardId())) {
			throw new IllegalArgumentException("WardId not existed");
		}

		if (!userAddressRepository.existsByUserId(userId)) {
			userAddressRequest.setIsDefault(true);
		} else if (Boolean.TRUE.equals(userAddressRequest.getIsDefault())) {
			userAddressRepository.setAllNotDefaultByUserId(userId);
		}

		UserAddressEntity entity = userAddressMapper.userAddressRequestToEntity(userAddressRequest);
		entity.setUser(UserEntity.builder().id(userId).build());
		UserAddressEntity savedEntity = userAddressRepository.save(entity);

		return userAddressMapper.entityToDomain(savedEntity);
	}

	@Transactional
	@Override
	public UserAddress update(Long userId, UserAddressRequest userAddressRequest) {
		if (!wardRepository.existsById(userAddressRequest.getWardId())) {
			throw new IllegalArgumentException("WardId not existed");
		}

		var oldEntity = userAddressRepository.findById(userAddressRequest.getId())
			.orElseThrow(() -> new EntityNotFoundException("Address not existed"));

		if (!userId.equals(oldEntity.getUser().getId())) {
			throw new AccessDeniedException("User not own this address");
		}

		if (Boolean.TRUE.equals(oldEntity.getIsDefault()) && !userAddressRequest.getIsDefault()) {
			throw new IllegalArgumentException("Cannot make default address become non default");
		}

		if (Boolean.TRUE.equals(userAddressRequest.getIsDefault())) {
			userAddressRepository.setAllNotDefaultByUserId(userId);
		}

		var newEntity = userAddressMapper.userAddressRequestToEntity(userAddressRequest);
		newEntity.setUser(oldEntity.getUser());
		return userAddressMapper.entityToDomain(userAddressRepository.save(newEntity));
	}

	@Transactional
	@Override
	public UserAddress delete(Long userId, Long id) {
		var entity = userAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Address not existed"));

		if (!userId.equals(entity.getUser().getId())) {
			throw new AccessDeniedException("User not own this address");
		}

		if (Boolean.TRUE.equals(entity.getIsDefault())) {
			throw new IllegalArgumentException("Cannot delete default address");
		}

		userAddressRepository.delete(entity);
		return userAddressMapper.entityToDomain(entity);
	}

}
