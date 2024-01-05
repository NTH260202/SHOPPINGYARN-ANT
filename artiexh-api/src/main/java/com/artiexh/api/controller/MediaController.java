package com.artiexh.api.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.service.media.FileStreamResponse;
import com.artiexh.api.service.media.StorageService;
import com.artiexh.model.domain.Role;
import com.artiexh.model.rest.media.FileResponseList;
import com.artiexh.model.rest.media.UpdateSharedUsersRequest;
import com.artiexh.model.rest.media.UploadRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoint.Media.ROOT)
public class MediaController {
	private final StorageService storageService;

	@PostMapping(path = Endpoint.Media.PUBLIC_UPLOAD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FileResponseList publicUpload(@Valid @ModelAttribute UploadRequest request) {
		try {
			return storageService.upload(request.getFile(), null);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UPLOAD_FAILED.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}

	}

	@PostMapping(path = Endpoint.Media.UPLOAD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FileResponseList upload(Authentication authentication,
								   @Valid @ModelAttribute UploadRequest request) {
		var userId = (Long) authentication.getPrincipal();
		try {
			return storageService.upload(request.getFile(), userId);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UPLOAD_FAILED.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}

	@PutMapping(path = Endpoint.Media.DETAIL)
	public void updateSharedUsers(Authentication authentication,
								  @PathVariable Long id,
								  @Valid @RequestBody UpdateSharedUsersRequest request) {
		var userId = (Long) authentication.getPrincipal();
		try {
			storageService.updateSharedUsers(userId, request.getSharedUserIds(), id);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.MEDIA_NOT_FOUND.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}

	@GetMapping("download/{id}")
	public ResponseEntity<byte[]> download(Authentication authentication, @PathVariable Long id) {
		var userId = (Long) authentication.getPrincipal();
		try {
			boolean isStaff = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals(Role.ADMIN.name()) || r.getAuthority().equals(Role.STAFF.name()));
			FileStreamResponse object = storageService.download(id, userId, isStaff);
			String contentType = object.getContentType();
			var bytes = object.getContent().readAllBytes();

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.valueOf(contentType));
			header.setContentLength(bytes.length);
			return ResponseEntity.ok().headers(header).body(bytes);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.DOWNLOAD_FAILED.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorCode.DOWNLOAD_NOT_ALLOWED.getMessage(), e);
		} catch (AmazonS3Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}
}
