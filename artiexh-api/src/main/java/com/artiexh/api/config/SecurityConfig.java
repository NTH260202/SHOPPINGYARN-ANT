package com.artiexh.api.config;

import com.artiexh.api.authentication.ArtiexhOAuth2UserService;
import com.artiexh.api.authentication.HttpCookieOAuth2AuthorizationRequestRepository;
import com.artiexh.api.authentication.OAuth2AuthenticationFailureHandler;
import com.artiexh.api.authentication.OAuth2AuthenticationSuccessHandler;
import com.artiexh.api.base.common.Endpoint;
import com.artiexh.auth.authentication.CookiesLogoutHandler;
import com.artiexh.auth.authentication.JwtAuthenticationFilter;
import com.artiexh.auth.authentication.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
										   JwtAuthenticationFilter jwtAuthenticationFilter,
										   HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
										   ArtiexhOAuth2UserService artiexhOAuth2UserService,
										   OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
										   OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
										   CookiesLogoutHandler cookiesLogoutHandler,
										   JwtAuthenticationProvider jwtAuthenticationProvider,
										   AuthenticationEntryPoint http401UnauthorizedEntryPoint,
										   LogoutSuccessHandler logoutSuccessHandler) throws Exception {
		httpSecurity.authenticationProvider(jwtAuthenticationProvider);
		httpSecurity
			.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.csrf(CsrfConfigurer::disable)
			.cors(Customizer.withDefaults())
			.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(FormLoginConfigurer::disable)
			.httpBasic(HttpBasicConfigurer::disable)
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/swagger*/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/error").permitAll()
				.requestMatchers(Endpoint.OAuth2.ROOT + "/**").permitAll()
				.requestMatchers(Endpoint.Registration.ROOT + "/**").permitAll()
				.requestMatchers(Endpoint.Auth.ROOT + Endpoint.Auth.LOGIN).permitAll()
				.requestMatchers(Endpoint.Auth.ROOT + Endpoint.Auth.REFRESH).permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Category.ROOT).permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Tag.ROOT).permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Province.ROOT).permitAll()
				.requestMatchers(Endpoint.Media.ROOT + Endpoint.Media.PUBLIC_UPLOAD).permitAll()
				.requestMatchers(Endpoint.Account.ROOT + Endpoint.Account.PROFILE).permitAll()
				.requestMatchers(HttpMethod.GET,
					Endpoint.Product.ROOT,
					Endpoint.Product.ROOT + Endpoint.Product.PRODUCT_DETAIL,
					Endpoint.Product.ROOT + Endpoint.Product.SUGGESTION).permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Shop.ROOT + "/**").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.ArtistCampaign.ROOT + "/{id}/product").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Shop.ROOT + "/{username}/campaign").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.ProviderCategory.ROOT + "/**").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Artist.ROOT + "/**").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Post.ROOT + "/**").permitAll()
				.requestMatchers(HttpMethod.GET, Endpoint.Marketplace.ROOT + "/**").permitAll()
				.requestMatchers("/ws/**").permitAll()
				.requestMatchers("/socket.io/**").permitAll()
				.requestMatchers("/order/shipment").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
					.baseUri(Endpoint.OAuth2.ROOT + Endpoint.OAuth2.AUTHORIZATION)
					.authorizationRequestRepository(authorizationRequestRepository)
				)
				.redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
					.baseUri(Endpoint.OAuth2.ROOT + Endpoint.OAuth2.CALLBACK)
				)
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(artiexhOAuth2UserService)
				)
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler)
			)
			.logout(logout -> logout
				.logoutUrl(Endpoint.Auth.ROOT + Endpoint.Auth.LOGOUT)
				.addLogoutHandler(cookiesLogoutHandler)
				.logoutSuccessHandler(logoutSuccessHandler)
			)
			.exceptionHandling(exceptionHandling ->
				exceptionHandling.authenticationEntryPoint(http401UnauthorizedEntryPoint)
			);
		return httpSecurity.build();
	}

}
