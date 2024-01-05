package com.artiexh.api.authentication;

import com.artiexh.data.jpa.repository.UserRepository;
import com.artiexh.model.domain.User;
import com.artiexh.model.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.util.Map;

@Service
@Log4j2
public class ArtiexhOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
	private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
	private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
	private static final String UNKNOWN_OAUTH2_PROVIDER = "unknown_oauth2_provider";
	private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
		new ParameterizedTypeReference<>() {
		};

	private final RestOperations restOperations;
	private final UserRepository userRepository;
	private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
	private final UserMapper userMapper;

	public ArtiexhOAuth2UserService(UserRepository userRepository, UserMapper mapper) {
		this.userRepository = userRepository;
		this.userMapper = mapper;
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		var userAttributes = getUserInfoAttributes(userRequest);

		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		String sub = userAttributes.get(userNameAttributeName).toString();
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		User user = queryUserByProviderId(sub, registrationId);
		if (user == null) {
			throw new Oauth2UserNotExistedException("User not existed", registrationId, userAttributes);
		}

		return new ArtiexhOAuth2User(user.getId().toString(), user.getUsername(), new SimpleGrantedAuthority(user.getRole().name()), userAttributes);
	}

	private Map<String, Object> getUserInfoAttributes(OAuth2UserRequest userRequest) {
		Assert.notNull(userRequest, "userRequest cannot be null");
		if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE, "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
		if (!StringUtils.hasText(userNameAttributeName)) {
			OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE, "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

		RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
		ResponseEntity<Map<String, Object>> response = getResponse(userRequest, request);
		Map<String, Object> userAttributes = response.getBody();
		if (userAttributes == null || !userAttributes.containsKey(userNameAttributeName)) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, "Missing required \"sub\" attribute in UserInfo response: " + userRequest.getClientRegistration().getRegistrationId(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}
		return userAttributes;
	}

	private ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
		try {
			return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
		} catch (OAuth2AuthorizationException ex) {
			OAuth2Error oauth2Error = ex.getError();
			StringBuilder errorDetails = new StringBuilder();
			errorDetails.append("Error details: [");
			errorDetails.append("UserInfo Uri: ")
				.append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
			errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
			if (oauth2Error.getDescription() != null) {
				errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
			}
			errorDetails.append("]");
			oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
				"An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails,
				null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
		} catch (UnknownContentTypeException ex) {
			String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '"
				+ userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
				+ "': response contains invalid content type '" + ex.getContentType() + "'. "
				+ "The UserInfo Response should return a JSON object (content type 'application/json') "
				+ "that contains a collection of name and value pairs of the claims about the authenticated End-User. "
				+ "Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '"
				+ userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, "
				+ "as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, errorMessage, null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
		} catch (RestClientException ex) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
				"An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
		}
	}

	private User queryUserByProviderId(String sub, String provider) {
		var userEntity = switch (provider) {
			case "google" -> userRepository.findByGoogleId(sub);
			case "facebook" -> userRepository.findByFacebookId(sub);
			default -> throw new OAuth2AuthenticationException(UNKNOWN_OAUTH2_PROVIDER);
		};
		return userEntity.map(userMapper::entityToBasicUser).orElse(null);
	}

}
