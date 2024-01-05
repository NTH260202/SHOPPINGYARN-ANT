package com.artiexh.api.controller.auth;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.AuthenticationService;
import com.artiexh.auth.common.CookieUtil;
import com.artiexh.auth.jwt.JwtProcessor;
import com.artiexh.auth.property.JwtConfiguration;
import com.artiexh.auth.service.ActiveTokenService;
import com.artiexh.model.domain.Account;
import com.artiexh.model.domain.Role;
import com.artiexh.model.rest.auth.LoginRequest;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.artiexh.auth.common.AuthConstant.ACCESS_TOKEN_COOKIE_NAME;
import static com.artiexh.auth.common.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;

@RestController
@RequestMapping(Endpoint.Auth.ROOT)
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final JwtConfiguration jwtConfiguration;
	private final JwtProcessor jwtProcessor;
	private final ActiveTokenService activeTokenService;

	@PostMapping(Endpoint.Auth.LOGIN)
	public Account login(HttpServletResponse response, @RequestBody @Valid LoginRequest loginRequest) {
		Account account = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

		String accessToken = jwtProcessor.encode(account.getId().toString(), account.getRole(), JwtProcessor.TokenType.ACCESS_TOKEN);
		CookieUtil.addCookies(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, (int) jwtConfiguration.getAccessTokenExpiration().getSeconds());

		String refreshToken = jwtProcessor.encode(account.getId().toString(), account.getRole(), JwtProcessor.TokenType.REFRESH_TOKEN);
		CookieUtil.addCookies(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, (int) jwtConfiguration.getRefreshTokenExpiration().getSeconds());

		activeTokenService.put(account.getId().toString(), accessToken, refreshToken);

		return account;
	}

	@PostMapping(Endpoint.Auth.REFRESH)
	public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = CookieUtil.getCookies(request, REFRESH_TOKEN_COOKIE_NAME)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is not found"));

		DecodedJWT decodedJwt;
		try {
			decodedJwt = jwtProcessor.decode(refreshToken);
		} catch (Exception ex) {
			log.trace("Failed to parse or decode token", ex);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
		}

		String sub = decodedJwt.getSubject();
		if (!activeTokenService.containRefreshToken(sub, refreshToken)) {
			log.trace("Token is destroyed");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is expired");
		}

		Role role = Role.valueOf(decodedJwt.getClaim("authority").asString());
		String newAccessToken = jwtProcessor.encode(sub, role, JwtProcessor.TokenType.ACCESS_TOKEN);
		String newRefreshToken = jwtProcessor.encode(sub, role, JwtProcessor.TokenType.REFRESH_TOKEN);

		activeTokenService.put(sub, newAccessToken, newRefreshToken);
		CookieUtil.addCookies(response, ACCESS_TOKEN_COOKIE_NAME, newAccessToken, (int) jwtConfiguration.getAccessTokenExpiration().getSeconds());
		CookieUtil.addCookies(response, REFRESH_TOKEN_COOKIE_NAME, newRefreshToken, (int) jwtConfiguration.getRefreshTokenExpiration().getSeconds());

		return ResponseEntity.ok().build();
	}

}
