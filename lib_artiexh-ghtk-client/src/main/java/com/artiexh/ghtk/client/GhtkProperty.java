package com.artiexh.ghtk.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "artiexh.ghtk")
public class GhtkProperty {
	private String baseUrl;
	private String token;
}
