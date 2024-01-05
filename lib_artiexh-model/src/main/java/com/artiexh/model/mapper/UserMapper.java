package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.SubscriptionEntity;
import com.artiexh.data.jpa.entity.UserEntity;
import com.artiexh.model.domain.User;
import com.artiexh.model.rest.account.AccountProfile;
import com.artiexh.model.rest.auth.RegisterUserRequest;
import org.hibernate.Hibernate;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {AccountMapper.class, ProductMapper.class, ProductAttachMapper.class, PasswordMapper.class}
)
public interface UserMapper {

	@Named("entityToBasicUser")
	@Mapping(target = "subscriptionsTo", ignore = true)
	@Mapping(target = "shoppingCart", ignore = true)
	User entityToBasicUser(UserEntity user);

	AccountProfile entityToAccountProfile(UserEntity userEntity);

	@Named("domainToAccountProfile")
	AccountProfile domainToAccountProfile(User userEntity);

	@Mapping(target = "shoppingCart", ignore = true)
	UserEntity domainToEntity(User user);

	@Mapping(target = "role", constant = "USER")
	@Mapping(target = "status", constant = "ACTIVE")
	@Mapping(source = "password", target = "password", qualifiedByName = "encodedPassword")
	User registerUserRequestToDomain(RegisterUserRequest request);

	@Condition
	default boolean isNotLazyLoadedSubscriptionsTo(Set<SubscriptionEntity> subscriptionsTo) {
		return Hibernate.isInitialized(subscriptionsTo);
	}

}
