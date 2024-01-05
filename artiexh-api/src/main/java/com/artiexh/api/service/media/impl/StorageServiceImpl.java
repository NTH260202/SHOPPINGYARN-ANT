package com.artiexh.api.service.media.impl;

import com.artiexh.api.base.exception.ErrorCode;
import com.artiexh.api.base.utils.S3Util;
import com.artiexh.api.service.media.FileAccessProxy;
import com.artiexh.api.service.media.FileStreamResponse;
import com.artiexh.api.service.media.StorageService;
import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.data.jpa.entity.MediaEntity;
import com.artiexh.data.jpa.repository.AccountRepository;
import com.artiexh.data.jpa.repository.MediaRepository;
import com.artiexh.model.rest.media.FileResponse;
import com.artiexh.model.rest.media.FileResponseList;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements StorageService {
	private final MediaRepository mediaRepository;
	private final AccountRepository accountRepository;
	private final FileAccessProxy fileAccessProxy;

	@Override
	@Transactional
	public FileResponse upload(MultipartFile multipartFile, Long userId) {
		File file = null;
		try {
			file = convertMultiPartToFile(multipartFile);
			String fileName = S3Util.generateFileName(multipartFile);
			String fileUrl;
			MediaEntity entity = null;

			if (userId == null) {
				fileUrl = fileAccessProxy.upload(file, fileName, true);
			} else {
				fileUrl = fileAccessProxy.upload(file, fileName, false);
				AccountEntity owner = accountRepository.getReferenceById(userId);
				entity = MediaEntity.builder()
					.owner(owner)
					.fileName(fileName)
					.name(multipartFile.getOriginalFilename().replace(" ", "_"))
					.sharedUsers(Collections.emptySet())
					.build();
				mediaRepository.save(entity);
			}

			return FileResponse.builder()
				.id(entity == null ? null : entity.getId())
				.presignedUrl(fileUrl)
				.name(entity == null ? "" : entity.getName())
				.fileName(fileName)
				.build();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			if (file != null) {
				file.delete();
			}
		}
	}

	@Override
	@Transactional
	public FileResponseList upload(List<MultipartFile> multipartFile, Long userId) {
		List<FileResponse> fileResponses = new ArrayList<>();
		for (MultipartFile file : multipartFile) {
			FileResponse fileResponse = upload(file, userId);
			fileResponses.add(fileResponse);
		}
		return FileResponseList.builder()
			.fileResponses(fileResponses)
			.build();
	}

	@Override
	@Transactional
	public void updateSharedUsers(Long userId, Long[] sharedIds, Long mediaId) {
		int numOfNotExisted = accountRepository.countByIdsIn(sharedIds);
		if (numOfNotExisted < sharedIds.length) {
			throw new IllegalArgumentException(ErrorCode.ACCOUNT_INFO_NOT_FOUND.getMessage());
		}
		MediaEntity entity = mediaRepository.findByIdAndOwnerId(mediaId, userId).orElseThrow(EntityNotFoundException::new);

		if (Arrays.stream(sharedIds).anyMatch(id -> id.equals(entity.getOwner().getId()))) {
			throw new IllegalArgumentException(ErrorCode.OWNER_NOT_ALLOWED.getMessage());
		}
		entity.setSharedUsers(Arrays.stream(sharedIds)
			.map(sharedId -> AccountEntity.builder().id(sharedId).build())
			.collect(Collectors.toSet()));
		mediaRepository.save(entity);
	}

	@Override
	public FileStreamResponse download(Long id, Long userId, boolean isStaff) throws IOException {
		String fileName;
		MediaEntity media;
		if (!isStaff) {
			media = mediaRepository.findByIdAndSharedUsersId(id, userId).orElseThrow(EntityNotFoundException::new);
		} else {
			media = mediaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		}
		fileName = media.getFileName();

		return fileAccessProxy.download(fileName);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
		try (FileOutputStream fos = new FileOutputStream(convFile)) {
			fos.write(file.getBytes());
			return convFile;
		}
	}
}
