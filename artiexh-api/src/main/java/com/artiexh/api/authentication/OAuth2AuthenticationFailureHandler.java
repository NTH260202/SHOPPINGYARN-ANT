package com.artiexh.api.authentication;

import com.artiexh.auth.common.CookieUtil;
import com.artiexh.auth.service.RecentOauth2LoginFailId;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.artiexh.auth.common.AuthConstant.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	private final RecentOauth2LoginFailId recentOauth2LoginFailId;
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


	public OAuth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, RecentOauth2LoginFailId recentOauth2LoginFailId) {
		this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
		this.recentOauth2LoginFailId = recentOauth2LoginFailId;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String targetUrl = CookieUtil.getCookies(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found or invalid redirect uri"));

		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);

		if (exception instanceof Oauth2UserNotExistedException oauth2UserNotExistedException) {
			var attributes = oauth2UserNotExistedException.getAttributes();
			var sub = (String) attributes.get("sub");
			var providerId = oauth2UserNotExistedException.getProviderId();
			String targetUrlWithSub = UriComponentsBuilder.fromUriString(targetUrl)
				.queryParam("providerId", providerId)
				.queryParam("sub", sub)
				.build().toUriString();
			recentOauth2LoginFailId.put(providerId, sub);
			redirectStrategy.sendRedirect(request, response, targetUrlWithSub);
		} else {
			String targetUrlWithError = UriComponentsBuilder.fromUriString(targetUrl)
				.queryParam("error", exception.getLocalizedMessage())
				.build().toUriString();
			redirectStrategy.sendRedirect(request, response, targetUrlWithError);
		}
	}

}
