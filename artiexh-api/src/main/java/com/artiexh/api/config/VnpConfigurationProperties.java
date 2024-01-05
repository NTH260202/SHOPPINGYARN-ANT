package com.artiexh.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "artiexh.payment.vnp")
@Data
public class VnpConfigurationProperties {
	private String tmnCode;
	private String version;
	private String command;
	private String returnUrl;
	private String url;
	private String secretKey;
}
