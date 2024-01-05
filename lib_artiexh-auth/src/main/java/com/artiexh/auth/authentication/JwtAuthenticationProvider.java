package com.artiexh.auth.authentication;

import com.artiexh.auth.jwt.JwtProcessor;
import com.artiexh.auth.service.ActiveTokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtProcessor jwtProcessor;
	private final ActiveTokenService activeTokenService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
		String token = authenticationToken.getCredentials().toString();

		DecodedJWT decodedJwt;
		try {
			decodedJwt = jwtProcessor.decode(token);
		} catch (JWTVerificationException ex) {
			log.trace("Failed to parse or decode token", ex);
			throw new BadCredentialsException("Failed to parse token", ex);
		}

		String sub = decodedJwt.getSubject();

		if (!activeTokenService.containAccessToken(sub, token)) {
			log.trace("Token is destroyed");
			throw new DisabledException("Token is signed out");
		}

		String authority = decodedJwt.getClaim("authority").asString();

		GrantedAuthority authorities = new SimpleGrantedAuthority(authority);
		return new JwtAuthenticationToken(Long.valueOf(sub), token, decodedJwt, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
