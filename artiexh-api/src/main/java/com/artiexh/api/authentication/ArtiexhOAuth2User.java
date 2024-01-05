package com.artiexh.api.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArtiexhOAuth2User implements OAuth2User, UserDetails {
	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final String id;
	private final String username;
	private final Set<? extends GrantedAuthority> authorities;
	private final Map<String, Object> attributes;

	public ArtiexhOAuth2User(String id, String username, GrantedAuthority authorities, Map<String, Object> attributes) {
		this.id = id;
		this.username = username;
		this.authorities = Set.of(authorities);
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return id;
	}
}
