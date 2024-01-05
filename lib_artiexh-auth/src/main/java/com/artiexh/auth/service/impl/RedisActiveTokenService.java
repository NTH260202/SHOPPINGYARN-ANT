package com.artiexh.auth.service.impl;

import com.artiexh.auth.property.JwtConfiguration;
import com.artiexh.auth.service.ActiveTokenService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisActiveTokenService implements ActiveTokenService {
	private static final String ACCESS_POSTFIX = "_access";
	private static final String REFRESH_POSTFIX = "_refresh";
	private final StringRedisTemplate redisTemplate;
	private final JwtConfiguration jwtConfiguration;

	public RedisActiveTokenService(StringRedisTemplate redisTemplate, JwtConfiguration jwtConfiguration) {
		this.redisTemplate = redisTemplate;
		this.jwtConfiguration = jwtConfiguration;
	}

	@Override
	public void put(String userId, String accessToken, String refreshToken) {
		redisTemplate.boundValueOps(userId + ACCESS_POSTFIX).set(accessToken, jwtConfiguration.getAccessTokenExpiration());
		redisTemplate.boundValueOps(userId + REFRESH_POSTFIX).set(refreshToken, jwtConfiguration.getRefreshTokenExpiration());
	}

	@Override
	public void remove(String userId) {
		redisTemplate.delete(userId + ACCESS_POSTFIX);
		redisTemplate.delete(userId + REFRESH_POSTFIX);
	}

	@Override
	public boolean containAccessToken(String userId, String accessToken) {
		String existedUserAccessToken = redisTemplate.boundValueOps(userId + ACCESS_POSTFIX).get();
		return accessToken.equals(existedUserAccessToken);
	}

	@Override
	public boolean containRefreshToken(String userId, String refreshToken) {
		String existedUserRefreshToken = redisTemplate.boundValueOps(userId + REFRESH_POSTFIX).get();
		return refreshToken.equals(existedUserRefreshToken);
	}
}
