package com.artiexh.api.authentication;

import com.artiexh.auth.common.CookieUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Base64;

import static com.artiexh.auth.common.AuthConstant.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static com.artiexh.auth.common.AuthConstant.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		Assert.notNull(request, "request cannot be null");
		return CookieUtil.getCookies(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
			.map(this::base64Deserialize)
			.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");

		if (authorizationRequest == null) {
			removeAuthorizationRequest(request, response);
		}

		CookieUtil.addCookies(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, base64Serialize(authorizationRequest));

		String redirectUri = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
		if (StringUtils.isNotBlank(redirectUri)) {
			CookieUtil.addCookies(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
		OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
		if (authorizationRequest != null) {
			CookieUtil.deleteCookies(request, response, REDIRECT_URI_PARAM_COOKIE_NAME, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		}
		return authorizationRequest;
	}

	private String base64Serialize(Serializable object) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}

	private OAuth2AuthorizationRequest base64Deserialize(String encodedStr) {
		return SerializationUtils.deserialize(Base64.getUrlDecoder().decode(encodedStr));
	}

}
