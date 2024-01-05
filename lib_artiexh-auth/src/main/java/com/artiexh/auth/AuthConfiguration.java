package com.artiexh.auth;

import com.artiexh.auth.property.ArtiexhCorsConfiguration;
import com.artiexh.auth.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ArtiexhCorsConfiguration.class, JwtConfiguration.class})
public class AuthConfiguration {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(ArtiexhCorsConfiguration artiexhCorsConfiguration) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(artiexhCorsConfiguration.getAllowedOrigins());
		configuration.setAllowedMethods(artiexhCorsConfiguration.getAllowedMethods());
		configuration.setAllowedHeaders(artiexhCorsConfiguration.getAllowedHeaders());
		configuration.setAllowCredentials(artiexhCorsConfiguration.isAllowCredentials());
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
