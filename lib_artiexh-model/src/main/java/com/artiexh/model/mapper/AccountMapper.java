package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.model.domain.Account;
import com.artiexh.model.domain.Role;
import com.artiexh.model.domain.UserStatus;
import com.artiexh.model.rest.auth.RegisterAdminRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {PasswordMapper.class}
)
public interface AccountMapper {

	Account entityToDomain(AccountEntity accountEntity);

	AccountEntity domainToEntity(Account account);

	@Mapping(target = "role", constant = "STAFF")
	@Mapping(target = "status", constant = "ACTIVE")
	@Mapping(source = "password", target = "password", qualifiedByName = "encodedPassword")
	Account registerStaffRequestToDomain(RegisterAdminRequest registerAdminRequest);

	default int toValue(Role role) {
		return role.getValue();
	}

	default Role toRole(int value) {
		return Role.fromValue(value);
	}

	default Integer toValue(UserStatus status) {
		return status.getValue();
	}

	default UserStatus toUserStatus(Integer value) {
		return UserStatus.fromValue(value);
	}

}