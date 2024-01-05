package com.artiexh.auth.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "artiexh.security.cors")
public class ArtiexhCorsConfiguration {
	private List<String> allowedOrigins;
	private List<String> allowedMethods;
	private List<String> allowedHeaders;
	private boolean allowCredentials;
}
