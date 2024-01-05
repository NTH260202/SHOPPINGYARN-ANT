package com.artiexh.api.config;

import com.artiexh.auth.property.ArtiexhCorsConfiguration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PreDestroy;

@CrossOrigin
@Configuration
@Slf4j
public class SocketIOConfig {

	@Value("${socket.port}")
	private Integer port;

	private SocketIOServer server;

	@Bean
	public SocketIOServer socketIOServer(ArtiexhCorsConfiguration artiexhCorsConfiguration) {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		config.setPort(port);
		config.setOrigin(String.join(",", artiexhCorsConfiguration.getAllowedOrigins()));
		server = new SocketIOServer(config);
		server.start();
		server.addConnectListener((client) -> {
			String privateRoom = client.getHandshakeData().getSingleUrlParam("userId");
			String groupRoom = client.getHandshakeData().getSingleUrlParam("room");
			if (StringUtils.isNotBlank(privateRoom)) {
				client.joinRoom(privateRoom);
			}
			if (StringUtils.isNotBlank(groupRoom)) {
				client.joinRoom(groupRoom);
			}
			log.info("Socket ID[{}]  Connected to socket ", client.getSessionId().toString());
		});
		server.addDisconnectListener(client ->
			client.getNamespace()
				.getAllClients()
				.stream()
				.forEach(data -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString())));
		return server;
	}

	@PreDestroy
	public void stopSocketIOServer() {
		this.server.stop();
	}
}
