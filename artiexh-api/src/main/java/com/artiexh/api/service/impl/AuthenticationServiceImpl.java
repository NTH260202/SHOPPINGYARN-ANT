package com.artiexh.api.service.impl;

import com.artiexh.api.service.AuthenticationService;
import com.artiexh.data.jpa.repository.AccountRepository;
import com.artiexh.model.domain.Account;
import com.artiexh.model.domain.UserStatus;
import com.artiexh.model.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final AccountMapper accountMapper;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Optional<Account> login(String username, String password) {
		return accountRepository.findByUsernameAndStatus(username, UserStatus.ACTIVE.getByteValue())
			.filter(user -> passwordEncoder.matches(password, user.getPassword()))
			.map(accountMapper::entityToDomain);
	}

}
