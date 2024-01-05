package com.artiexh.api.service.media.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PresignedUrlDownloadRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.artiexh.api.base.utils.S3Util;
import com.artiexh.api.config.S3ConfigurationProperties;
import com.artiexh.api.service.media.FileAccessStrategy;
import com.artiexh.api.service.media.FileStreamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class S3FileAccessStrategy implements FileAccessStrategy {
	private final S3ConfigurationProperties s3Config;
	private final AmazonS3 s3;

	@Override
	public String upload(File file, String fileName, boolean isPublic) {
		if (isPublic) {
			s3.putObject(new PutObjectRequest(s3Config.getPublicBucketName(), fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
			return S3Util.getPresignedString(
				s3Config.getRegion(),
				s3Config.getPublicBucketName(),
				s3Config.getAccessKey(),
				s3Config.getSecretKey(),
				fileName,
				true
			);
		} else {
			s3.putObject(new PutObjectRequest(s3Config.getPrivateBucketName(), fileName, file));
			return null;
		}
	}

	@Override
	public FileStreamResponse download(String fileName) throws MalformedURLException {
		URL url = new URL(S3Util.getPresignedString(
			s3Config.getRegion(), s3Config.getPrivateBucketName(),
			s3Config.getAccessKey(), s3Config.getSecretKey(),
			fileName,
			false
		));
		S3Object object = s3.download(new PresignedUrlDownloadRequest(url)).getS3Object();
		return new FileStreamResponse(object.getObjectContent(), object.getObjectMetadata().getContentType());
	}
}
