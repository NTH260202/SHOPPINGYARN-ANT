package com.artiexh.api.service.notification.impl;

import com.artiexh.api.service.notification.NotificationJpaService;
import com.artiexh.data.jpa.entity.NotificationEntity;
import com.artiexh.data.jpa.repository.NotificationRepository;
import com.artiexh.model.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationJpaServiceImpl implements NotificationJpaService {
	private final NotificationRepository notificationRepository;
	private final NotificationMapper notificationMapper;

	@Override
	@Transactional
	public void markedAsRead(Long notificationId) {
		notificationRepository.markedAsRead(notificationId);
	}

	@Override
	public Page<NotificationEntity> getAll(Long userId, Pageable pageable) {
		return notificationRepository.findNotificationEntitiesByOwnerId(userId, pageable);
	}

	@Override
	public int unreadCount(Long userId) {
		return notificationRepository.countUnreadMessages(userId);
	}

	@Override
	@Transactional
	public NotificationEntity save(NotificationEntity message) {
		return notificationRepository.save(message);
	}

	@Override
	@Transactional
	public void saveAll(Set<NotificationEntity> messages) {
		notificationRepository.saveAll(messages);
	}
}
