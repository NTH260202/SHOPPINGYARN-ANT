package com.artiexh.auth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenResolver jwtTokenResolver = new CookieJwtTokenResolver();
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
	private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = jwtTokenResolver.resolve(request);
		if (token == null) {
			log.trace("Did not process request since did not find bearer token");
			filterChain.doFilter(request, response);
			return;
		}

		JwtAuthenticationToken authenticationRequest = new JwtAuthenticationToken(token);
		try {
			Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
			SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
			context.setAuthentication(authenticationResult);
			this.securityContextHolderStrategy.setContext(context);
			this.securityContextRepository.saveContext(context, request, response);
			if (log.isDebugEnabled()) {
				log.debug("Set SecurityContextHolder to " + authenticationResult);
			}
		} catch (AuthenticationException ex) {
			this.securityContextHolderStrategy.clearContext();
			log.trace("Failed to process authentication request", ex);
		}

		filterChain.doFilter(request, response);
	}

}
