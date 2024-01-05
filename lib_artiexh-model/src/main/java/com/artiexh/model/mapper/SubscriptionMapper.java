package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.SubscriptionEntity;
import com.artiexh.model.domain.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {UserMapper.class}
)
public interface SubscriptionMapper {

	@Mapping(target = "artist", ignore = true)
	@Mapping(target = "user", qualifiedByName = "entityToBasicUser")
	Subscription subscriptionEntityToArtistSubscription(SubscriptionEntity entity);


}
