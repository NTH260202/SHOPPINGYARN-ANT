package com.artiexh.api.service.notification;

import com.artiexh.model.domain.NotificationMessage;

public interface PushNotificationService {
	void sendTo(Long userId, NotificationMessage message);
	void sendTo(String groupName, NotificationMessage message);
}
