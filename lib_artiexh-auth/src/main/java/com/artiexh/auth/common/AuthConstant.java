package com.artiexh.auth.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class AuthConstant {

	public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String ACCESS_TOKEN_COOKIE_NAME = "artiexh_access_token";
	public static final String REFRESH_TOKEN_COOKIE_NAME = "artiexh_refresh_token";

}
