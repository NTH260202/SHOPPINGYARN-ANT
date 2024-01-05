package com.artiexh.api.authentication;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

@Getter
public class Oauth2UserNotExistedException extends AuthenticationException {

	private final String providerId;
	private final Map<String, Object> attributes;

	public Oauth2UserNotExistedException(String msg, String providerId, Map<String, Object> attributes) {
		super(msg);
		this.attributes = attributes;
		this.providerId = providerId;
	}

}
