package com.artiexh.api.service.notification;

import com.artiexh.data.jpa.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface NotificationJpaService {
	void markedAsRead(Long notificationId);

	Page<NotificationEntity> getAll(Long userId, Pageable pageable);

	int unreadCount(Long userId);

	NotificationEntity save(NotificationEntity message);

	void saveAll(Set<NotificationEntity> message);
}
