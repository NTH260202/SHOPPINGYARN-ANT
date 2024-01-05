package com.artiexh.api.authentication;

import com.artiexh.auth.common.CookieUtil;
import com.artiexh.auth.jwt.JwtProcessor;
import com.artiexh.auth.property.JwtConfiguration;
import com.artiexh.auth.service.ActiveTokenService;
import com.artiexh.model.domain.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.artiexh.auth.common.AuthConstant.ACCESS_TOKEN_COOKIE_NAME;
import static com.artiexh.auth.common.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class ResponseTokenProcessor {

	private final ActiveTokenService activeTokenService;
	private final JwtProcessor jwtProcessor;
	private final JwtConfiguration jwtConfiguration;

	public void process(HttpServletResponse response, String sub, Role role) {
		String accessToken = jwtProcessor.encode(sub, role, JwtProcessor.TokenType.ACCESS_TOKEN);
		CookieUtil.addCookies(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, (int) jwtConfiguration.getAccessTokenExpiration().getSeconds());

		String refreshToken = jwtProcessor.encode(sub, role, JwtProcessor.TokenType.REFRESH_TOKEN);
		CookieUtil.addCookies(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, (int) jwtConfiguration.getRefreshTokenExpiration().getSeconds());

		activeTokenService.put(sub, accessToken, refreshToken);
	}
}
