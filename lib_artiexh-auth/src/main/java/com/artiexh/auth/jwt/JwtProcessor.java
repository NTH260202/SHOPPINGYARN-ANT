package com.artiexh.auth.jwt;

import com.artiexh.model.domain.Role;
import com.auth0.jwt.interfaces.DecodedJWT;

public interface JwtProcessor {

	String encode(String id, Role role, TokenType type);

	DecodedJWT decode(String token);

	enum TokenType {
		ACCESS_TOKEN,
		REFRESH_TOKEN
	}

}
