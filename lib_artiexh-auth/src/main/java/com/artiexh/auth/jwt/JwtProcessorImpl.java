package com.artiexh.auth.jwt;

import com.artiexh.auth.property.JwtConfiguration;
import com.artiexh.model.domain.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
class JwtProcessorImpl implements JwtProcessor {

	private final JwtConfiguration configuration;
	private final Algorithm algorithm;
	private final JWTVerifier verifier;

	public JwtProcessorImpl(JwtConfiguration configuration) {
		this.configuration = configuration;
		algorithm = Algorithm.HMAC256(configuration.getSecretKey());
		verifier = JWT.require(algorithm)
			.withIssuer(configuration.getIssuer())
			.withAudience(configuration.getAudiences())
			.withClaimPresence("sub")
			.withClaimPresence("authority")
			.build();
	}

	@Override
	public String encode(String id, Role role, TokenType type) {
		Instant now = Instant.now();
		Duration expiration = switch (type) {
			case ACCESS_TOKEN -> configuration.getAccessTokenExpiration();
			case REFRESH_TOKEN -> configuration.getRefreshTokenExpiration();
		};
		return JWT.create()
			.withIssuer(configuration.getIssuer())
			.withAudience(configuration.getAudiences())
			.withSubject(id)
			.withClaim("authority", role.name())
			.withJWTId(UUID.randomUUID().toString())
			.withIssuedAt(now)
			.withExpiresAt(now.plus(expiration))
			.sign(algorithm);
	}

	@Override
	public DecodedJWT decode(String token) {
		return verifier.verify(token);
	}

}
