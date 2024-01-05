package com.artiexh.api.service.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.AccountService;
import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.data.jpa.entity.StaffEntity;
import com.artiexh.data.jpa.entity.UserEntity;
import com.artiexh.data.jpa.repository.AccountRepository;
import com.artiexh.data.jpa.repository.CartItemRepository;
import com.artiexh.data.jpa.repository.SubscriptionRepository;
import com.artiexh.model.domain.*;
import com.artiexh.model.mapper.AccountMapper;
import com.artiexh.model.mapper.ArtistMapper;
import com.artiexh.model.mapper.StaffMapper;
import com.artiexh.model.mapper.UserMapper;
import com.artiexh.model.rest.account.AccountProfile;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	private final UserMapper userMapper;
	private final ArtistMapper artistMapper;
	private final AccountMapper accountMapper;
	private final SubscriptionRepository subscriptionRepository;
	private final CartItemRepository cartItemRepository;
	private final StaffMapper staffMapper;

	@Override
	@Transactional
	public Account getUserById(Long id) {
		return accountRepository.findById(id)
			.map(accountEntity -> switch (Role.fromValue(accountEntity.getRole())) {
				case ADMIN -> accountMapper.entityToDomain(accountEntity);
				case USER -> {
					User user = userMapper.entityToBasicUser((UserEntity) accountEntity);
					user.setCartItemCount(cartItemRepository.countAllByCartId(id));
					yield user;
				}
				case ARTIST -> {
					Artist artist = artistMapper.basicArtistInfo((ArtistEntity) accountEntity);
					artist.setCartItemCount(cartItemRepository.countAllByCartId(id));
					yield artist;
				}
				case STAFF -> staffMapper.basicStaffInfo((StaffEntity) accountEntity);
			})
			.orElse(null);
	}

	@Override
	public AccountProfile getUserProfile(Long id) {
		AccountEntity entity = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		return switch (Role.fromValue(entity.getRole())) {
			case USER -> {
				AccountProfile profile = userMapper.entityToAccountProfile((UserEntity) entity);
				profile.setNumOfSubscriptions(subscriptionRepository.countByUserId(entity.getId()));
				yield profile;
			}
			case ARTIST -> {
				AccountProfile profile = artistMapper.entityToAccountProfile((ArtistEntity) entity);
				profile.setNumOfSubscriptions(subscriptionRepository.countByUserId(entity.getId()));
				yield profile;
			}
			case STAFF, ADMIN -> {
				yield staffMapper.entityToProfile((StaffEntity) entity);
			}
			default -> null;
		};
	}

	@Override
	@Transactional
	public AccountProfile updateProfile(Long id, AccountProfile accountProfile) {
		AccountEntity entity = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);

		entity.setDisplayName(accountProfile.getDisplayName());
		entity.setEmail(accountProfile.getEmail());
		entity.setAvatarUrl(accountProfile.getAvatarUrl());

		accountRepository.save(entity);
		AccountProfile profile = userMapper.entityToAccountProfile((UserEntity) entity);
		profile.setNumOfSubscriptions(subscriptionRepository.countByUserId(entity.getId()));
		return profile;
	}

	@Override
	public Page<Account> getAll(Specification<AccountEntity> specification, Pageable pageable) {
		Page<AccountEntity> accounts = accountRepository.findAll(specification, pageable);
		return accounts.map(accountMapper::entityToDomain);
	}

	@Override
	@Transactional
	public void updateAccountStatus(Long accountId, UserStatus status, Long updatedAccountId) {
		AccountEntity account = accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);
		AccountEntity updatedAccount = accountRepository.findById(updatedAccountId).orElseThrow(EntityNotFoundException::new);

		if (updatedAccount.getRole().equals(Role.STAFF.getByteValue()) && !Role.ALLOWED_STAFF_UPDATED_USER_STATUS.contains(Role.fromValue(account.getRole()))) {
			throw new InvalidException(ErrorCode.ACCOUNT_NOT_ALLOWED_UPDATED);
		}

		switch (status) {
			case BANNED -> {
				if (!Objects.equals(account.getStatus(), UserStatus.ACTIVE.getByteValue())) {
					throw new InvalidException(ErrorCode.USER_STATUS_NOT_ALLOWED_UPDATED);
				}
				account.setStatus(status.getByteValue());
			}
			case ACTIVE -> {
				if (!Objects.equals(account.getStatus(), UserStatus.BANNED.getByteValue())) {
					throw new InvalidException(ErrorCode.USER_STATUS_NOT_ALLOWED_UPDATED);
				}
				account.setStatus(status.getByteValue());
			}
			default -> throw new InvalidException(ErrorCode.USER_STATUS_NOT_ALLOWED_UPDATED);
		}

		accountRepository.save(account);
	}
}
