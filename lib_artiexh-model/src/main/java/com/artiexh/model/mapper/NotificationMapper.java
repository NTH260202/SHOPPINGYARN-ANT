package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.NotificationEntity;
import com.artiexh.model.domain.NotificationMessage;
import com.artiexh.model.domain.NotificationType;
import com.artiexh.model.rest.notification.MessageRequest;
import com.artiexh.model.rest.notification.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {
	@Mapping(target = "owner.id", source = "ownerId")
	NotificationEntity domainToEntity(NotificationMessage message);

	MessageResponse domainToResponse(NotificationEntity notification);

	NotificationMessage requestToDomain(MessageRequest messageRequest);

	default Byte toValue(NotificationType type) {
		return type.getByteValue();
	}

	default NotificationType notificationTypeFrom(byte value) {
		return NotificationType.fromValue(value);
	}
}
