package com.artiexh.api.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(S3ConfigurationProperties.class)
@RequiredArgsConstructor
public class S3Config {
	private final S3ConfigurationProperties s3Properties;

	@Bean
	public AmazonS3 initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.s3Properties.getAccessKey(), this.s3Properties.getSecretKey());
		return AmazonS3ClientBuilder
			.standard()
			.withRegion(s3Properties.getRegion())
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.build();
	}
}
