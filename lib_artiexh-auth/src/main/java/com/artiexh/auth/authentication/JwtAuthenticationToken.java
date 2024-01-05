package com.artiexh.auth.authentication;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken implements Authentication, CredentialsContainer {
	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Long principal;
	private final Collection<GrantedAuthority> authorities;
	private final boolean authenticated;
	private String credentials;
	private DecodedJWT details;

	public JwtAuthenticationToken(String credentials) {
		this.principal = null;
		this.credentials = credentials;
		this.details = null;
		this.authorities = AuthorityUtils.NO_AUTHORITIES;
		this.authenticated = false;
	}

	public JwtAuthenticationToken(Long principal, String credentials, DecodedJWT details, GrantedAuthority authority) {
		this.principal = principal;
		this.credentials = credentials;
		this.details = details;
		Assert.notNull(authority, "Authorities collection cannot contain any null elements");
		this.authorities = Collections.singletonList(authority);
		this.authenticated = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getDetails() {
		return details;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
	}

	@Override
	public String getName() {
		return getPrincipal().toString();
	}

	@Override
	public void eraseCredentials() {
		credentials = null;
		details = null;
	}

}
