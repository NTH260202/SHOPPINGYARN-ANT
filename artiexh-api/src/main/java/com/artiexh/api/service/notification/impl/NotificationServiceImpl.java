package com.artiexh.api.service.notification.impl;

import com.artiexh.api.base.exception.IllegalAccessException;
import com.artiexh.api.service.notification.NotificationJpaService;
import com.artiexh.api.service.notification.NotificationService;
import com.artiexh.api.service.notification.PushNotificationService;
import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.data.jpa.entity.NotificationEntity;
import com.artiexh.data.jpa.repository.AccountRepository;
import com.artiexh.model.domain.NotificationMessage;
import com.artiexh.model.domain.NotificationType;
import com.artiexh.model.domain.Role;
import com.artiexh.model.mapper.NotificationMapper;
import com.artiexh.model.rest.notification.MessagePageResponse;
import com.artiexh.model.rest.notification.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final NotificationJpaService notificationJpaService;
	private final AccountRepository accountRepository;
	private final PushNotificationService pushNotificationService;
	private final NotificationMapper notificationMapper;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@Override
	@Async
	public void sendAll(Role group, NotificationMessage message) {
		if (group.equals(Role.STAFF)) {
			group = Role.ADMIN;
		}
		List<AccountEntity> accounts = accountRepository.findAccountEntitiesByRole(group.getByteValue());
		message.setType(NotificationType.GROUP);
		for (AccountEntity account : accounts) {
			NotificationEntity notification = notificationMapper.domainToEntity(message);
			notification.setOwner(account);
			notificationJpaService.save(notification);
		}
		pushNotificationService.sendTo(group.name().toLowerCase(), message);
	}

	@Override
	@Async
	public void sendTo(Long userId, NotificationMessage message) {
		log.info("Send message to {}", userId);
		NotificationEntity notification = notificationJpaService.save(notificationMapper.domainToEntity(message));
		message.setId(notification.getId());
		pushNotificationService.sendTo(userId, message);
	}

	@Override
	@Async
	public void sendTo(List<Long> userId, NotificationMessage message) {
		List<AccountEntity> accounts = accountRepository.findAllById(userId);
		for (AccountEntity account : accounts) {
			NotificationEntity notification = notificationMapper.domainToEntity(message);
			notification.setOwner(account);
			notificationJpaService.save(notification);
			pushNotificationService.sendTo(account.getId(), message);
		}
	}

	@Override
	public MessagePageResponse<MessageResponse> getAll(Long userId, Role role, Pageable pageable) {
		if (Role.ADMIN.equals(role) || Role.STAFF.equals(role)) {
			userId = rootAdminId;
		}
		Page<MessageResponse> notificationPage = notificationJpaService.getAll(userId, pageable).map(notificationMapper::domainToResponse);
		int unreadCount = notificationJpaService.unreadCount(userId);
		return new MessagePageResponse<>(notificationPage, unreadCount);
	}

	@Override
	public void markedAsRead(Long userId, Long notificationId) {
		if (accountRepository.findById(userId).isEmpty()) {
			throw new IllegalAccessException();
		}
		notificationJpaService.markedAsRead(notificationId);
	}
}
