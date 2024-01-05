package com.artiexh.auth.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "artiexh.security.jwt")
@Data
public class JwtConfiguration {
	private String secretKey;
	private String issuer;
	private String[] audiences;
	@DurationUnit(ChronoUnit.MINUTES)
	private Duration accessTokenExpiration;
	@DurationUnit(ChronoUnit.MINUTES)
	private Duration refreshTokenExpiration;
}
