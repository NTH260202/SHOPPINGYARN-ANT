package com.artiexh.auth.authentication;

import com.artiexh.auth.common.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;

import static com.artiexh.auth.common.AuthConstant.ACCESS_TOKEN_COOKIE_NAME;

class CookieJwtTokenResolver implements JwtTokenResolver {

	@Override
	public String resolve(HttpServletRequest request) {
		return CookieUtil.getCookies(request, ACCESS_TOKEN_COOKIE_NAME).orElse(null);
	}

}
