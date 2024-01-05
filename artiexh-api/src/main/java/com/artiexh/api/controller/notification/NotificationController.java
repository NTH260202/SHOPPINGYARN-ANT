package com.artiexh.api.controller.notification;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.IllegalAccessException;
import com.artiexh.api.service.notification.NotificationService;
import com.artiexh.model.domain.NotificationMessage;
import com.artiexh.model.domain.Role;
import com.artiexh.model.mapper.NotificationMapper;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.notification.MessagePageResponse;
import com.artiexh.model.rest.notification.MessageRequest;
import com.artiexh.model.rest.notification.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = Endpoint.Notification.ROOT)
public class NotificationController {
	private final NotificationService notificationService;
	private final NotificationMapper notificationMapper;

	@GetMapping()
	public MessagePageResponse<MessageResponse> getAllMessages(
		Authentication authentication,
		@ParameterObject @Valid PaginationAndSortingRequest pagination) {
		long userId = (long) authentication.getPrincipal();
		return notificationService.getAll(userId, extractRole(authentication).orElse(null), pagination.getPageable());
	}

	@PutMapping("/{id}")
	public void markedAsRead(
		@PathVariable("id") Long id,
		Authentication authentication) {
		long userId = (long) authentication.getPrincipal();
		try {
			notificationService.markedAsRead(userId, id);
		} catch (IllegalAccessException exception) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping()
	@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
	public void pushNotification(@Valid @RequestBody MessageRequest notificationMessage) {
		try {
			notificationService.sendAll(notificationMessage.getGroup(), notificationMapper.requestToDomain(notificationMessage));
		} catch (IllegalAccessException exception) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}

	private Optional<Role> extractRole(Authentication authentication) {
		return authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.findFirst()
			.map(Role::valueOf);
	}
}
