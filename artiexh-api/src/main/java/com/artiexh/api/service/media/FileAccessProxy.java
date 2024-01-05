package com.artiexh.api.service.media;

import com.artiexh.api.base.common.Const;
import com.artiexh.api.base.service.SystemConfigService;
import com.artiexh.api.service.media.impl.S3FileAccessStrategy;
import com.artiexh.api.service.media.impl.SystemFileAccessStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileAccessProxy implements FileAccessStrategy {
	private static final String S3 = "s3";
	private static final String SYSTEM = "system";

	private final SystemConfigService systemConfigService;
	private final S3FileAccessStrategy s3FileAccessStrategy;
	private final SystemFileAccessStrategy systemFileAccessStrategy;

	@Override
	public String upload(File file, String fileName, boolean isPublic) throws IOException {
		String strategy = systemConfigService.getOrDefault(Const.SystemConfigKey.FILE_ACCESS_STRATEGY, S3);

		return switch (strategy) {
			case S3 -> s3FileAccessStrategy.upload(file, fileName, isPublic);
			case SYSTEM -> systemFileAccessStrategy.upload(file, fileName, isPublic);
			default -> throw new IllegalArgumentException("Unknown file access strategy: " + strategy);
		};
	}

	@Override
	public FileStreamResponse download(String fileName) throws IOException {
		String strategy = systemConfigService.getOrDefault(Const.SystemConfigKey.FILE_ACCESS_STRATEGY, S3);
		return switch (strategy) {
			case S3 -> s3FileAccessStrategy.download(fileName);
			case SYSTEM -> systemFileAccessStrategy.download(fileName);
			default -> throw new IllegalArgumentException("Unknown file access strategy: " + strategy);
		};
	}

}
