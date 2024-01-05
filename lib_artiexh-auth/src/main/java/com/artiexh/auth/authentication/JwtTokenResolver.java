package com.artiexh.auth.authentication;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenResolver {
	String resolve(HttpServletRequest request);
}
