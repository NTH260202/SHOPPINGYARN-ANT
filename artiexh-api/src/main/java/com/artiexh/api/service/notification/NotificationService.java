package com.artiexh.api.service.notification;

import com.artiexh.model.domain.NotificationMessage;
import com.artiexh.model.domain.NotificationType;
import com.artiexh.model.domain.Role;
import com.artiexh.model.rest.notification.MessagePageResponse;
import com.artiexh.model.rest.notification.MessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
	void sendAll(Role group, NotificationMessage message);

	void sendTo(Long userId, NotificationMessage message);

	void sendTo(List<Long> userId, NotificationMessage message);

	MessagePageResponse<MessageResponse> getAll(Long userId, Role role, Pageable pageable);

	void markedAsRead(Long userId, Long notificationId);
}
