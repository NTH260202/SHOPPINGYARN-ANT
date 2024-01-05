package com.artiexh.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "artiexh.aws.s3")
@Data
public class S3ConfigurationProperties {
	@Value("${artiexh.aws.s3.region}")
	private String region;
	@Value("${artiexh.aws.s3.public-bucket-name}")
	private String publicBucketName;
	@Value("${artiexh.aws.s3.private-bucket-name}")
	private String privateBucketName;
	@Value("${artiexh.aws.s3.access-key}")
	private String accessKey;
	@Value("${artiexh.aws.s3.secret-key}")
	private String secretKey;
}
