package com.artiexh.api.controller.auth;

import com.artiexh.api.authentication.ResponseTokenProcessor;
import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.exception.InvalidException;
import com.artiexh.api.service.RegistrationService;
import com.artiexh.model.domain.Account;
import com.artiexh.model.domain.Artist;
import com.artiexh.model.domain.User;
import com.artiexh.model.mapper.AccountMapper;
import com.artiexh.model.mapper.UserMapper;
import com.artiexh.model.rest.artist.request.RegistrationArtistRequest;
import com.artiexh.model.rest.auth.RegisterAdminRequest;
import com.artiexh.model.rest.auth.RegisterUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.Registration.ROOT)
public class RegistrationController {

	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RegistrationService registrationService;
	@Autowired
	private ResponseTokenProcessor responseTokenProcessor;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@PostMapping(Endpoint.Registration.USER)
	public User registerUser(HttpServletResponse response, @RequestBody @Valid RegisterUserRequest registerUserRequest) {
		try {
			User user = registrationService.createUser(userMapper.registerUserRequestToDomain(registerUserRequest));
			responseTokenProcessor.process(response, user.getId().toString(), user.getRole());
			return user;
		} catch (EntityExistsException ex) {
			throw new InvalidException(ErrorCode.USER_NAME_EXISTED);
		}
	}

	@PostMapping(Endpoint.Registration.ARTIST)
	@PreAuthorize("hasAuthority('USER')")
	public Artist registerArtist(Authentication authentication, @RequestBody @Valid RegistrationArtistRequest request
	) {
		Long id = (Long) authentication.getPrincipal();
		try {
			return registrationService.registerArtist(id, request);
		} catch (EntityNotFoundException ex) {
			throw new InvalidException(ErrorCode.USER_NOT_FOUND);
		}
	}

	@PostMapping(Endpoint.Registration.STAFF)
	@PreAuthorize("hasAuthority('ADMIN')")
	public Account registerStaff(Authentication authentication, @RequestBody @Valid RegisterAdminRequest registerAdminRequest) {
		if (rootAdminId.equals(authentication.getPrincipal())) {
			return registrationService.createStaff(accountMapper.registerStaffRequestToDomain(registerAdminRequest));
		} else {
			throw new InvalidException(ErrorCode.STAFF_REGISTRATION_NOT_ALLOWED);
		}

	}

}
