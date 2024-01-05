package com.artiexh.auth.service;

public interface ActiveTokenService {
	void put(String userId, String accessToken, String refreshToken);

	void remove(String userId);

	boolean containAccessToken(String userId, String accessToken);

	boolean containRefreshToken(String userId, String refreshToken);
}
