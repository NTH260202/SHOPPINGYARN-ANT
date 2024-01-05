package com.artiexh.auth.authentication;

import com.artiexh.auth.common.CookieUtil;
import com.artiexh.auth.jwt.JwtProcessor;
import com.artiexh.auth.service.ActiveTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import static com.artiexh.auth.common.AuthConstant.ACCESS_TOKEN_COOKIE_NAME;
import static com.artiexh.auth.common.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class CookiesLogoutHandler implements LogoutHandler {

	private final ActiveTokenService activeTokenService;
	private final JwtProcessor jwtProcessor;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		CookieUtil.getCookies(request, ACCESS_TOKEN_COOKIE_NAME).ifPresent(accessToken -> {
			String userId = jwtProcessor.decode(accessToken).getSubject();
			activeTokenService.remove(userId);
		});

		CookieUtil.deleteCookies(request, response, ACCESS_TOKEN_COOKIE_NAME, REFRESH_TOKEN_COOKIE_NAME);
	}
}
