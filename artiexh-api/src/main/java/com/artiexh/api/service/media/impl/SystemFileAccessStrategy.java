package com.artiexh.api.service.media.impl;

import com.artiexh.api.service.media.FileAccessStrategy;
import com.artiexh.api.service.media.FileStreamResponse;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class SystemFileAccessStrategy implements FileAccessStrategy {
	private static final String PRIVATE_PATH = "private/";
	private static final String PUBLIC_PATH = "public/";
	private static final Tika TIKA = new Tika();


	static {
		try {
			Files.createDirectories(Paths.get(PRIVATE_PATH));
			Files.createDirectories(Paths.get(PUBLIC_PATH));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Value("${artiexh.file-server.path}")
	private String fileServerPath;

	@Override
	public String upload(File file, String fileName, boolean isPublic) throws IOException {
		if (isPublic) {
			Path copiedPath = Paths.get(PUBLIC_PATH + fileName);
			Files.copy(file.toPath(), copiedPath, StandardCopyOption.REPLACE_EXISTING);
			return fileServerPath + copiedPath;
		} else {
			Path copiedPath = Paths.get(PRIVATE_PATH + fileName);
			Files.copy(file.toPath(), copiedPath, StandardCopyOption.REPLACE_EXISTING);
			return null;
		}
	}

	@Override
	public FileStreamResponse download(String fileName) throws IOException {
		Path path = Paths.get(PRIVATE_PATH + fileName);
		InputStream inputStream = Files.newInputStream(path);
		return new FileStreamResponse(inputStream, TIKA.detect(path));
	}
}
