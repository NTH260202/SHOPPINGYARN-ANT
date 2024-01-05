package com.artiexh.api.service.notification.impl;

import com.artiexh.api.service.notification.PushNotificationService;
import com.artiexh.model.domain.NotificationMessage;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationServiceImpl implements PushNotificationService {
	private final SocketIOServer server;
	private final String RECEIVE_EVENT = "messages";

//	public void sendMessage(String room, String eventName, SocketIOClient senderClient, String message) {
//		for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
//			if (!client.getSessionId().equals(senderClient.getSessionId())) {
//				client.sendEvent(eventName,
//					NotificationMessage.builder()
//						.title("Arty User")
//						.content("Hi, nice to meet you")
//						.build());
//			}
//		}
//	}

	@Override
	public void sendTo(Long userId, NotificationMessage message) {
		for (SocketIOClient client : server.getRoomOperations(userId.toString()).getClients()) {
			log.info("Sending notification for user {}", userId);
			client.sendEvent(RECEIVE_EVENT, message);
		}
	}

	@Override
	public void sendTo(String groupName, NotificationMessage message) {
		for (SocketIOClient client : server.getRoomOperations(groupName).getClients()) {
			log.info("Sending notification for group {}", groupName);
			client.sendEvent(RECEIVE_EVENT, message);
		}
	}
}
