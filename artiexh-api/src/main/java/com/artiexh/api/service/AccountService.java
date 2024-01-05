package com.artiexh.api.service;

import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.model.domain.Account;
import com.artiexh.model.domain.UserStatus;
import com.artiexh.model.rest.account.AccountProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface AccountService {

	Account getUserById(Long id);

	AccountProfile getUserProfile(Long id);

	AccountProfile updateProfile(Long id, AccountProfile profile);

	Page<Account> getAll(Specification<AccountEntity> specification, Pageable pageable);

	void updateAccountStatus(Long accountId, UserStatus status, Long updatedAccountId);
}
