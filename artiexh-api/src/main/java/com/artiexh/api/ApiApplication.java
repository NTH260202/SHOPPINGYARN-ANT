package com.artiexh.api;

import com.artiexh.api.config.VnpConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
@ComponentScan(basePackages = "com.artiexh")
@EntityScan(basePackages = "com.artiexh.data.jpa.entity")
@EnableJpaRepositories(basePackages = "com.artiexh.data.jpa.repository")
@EnableJpaAuditing
@EnableConfigurationProperties(VnpConfigurationProperties.class)
@EnableScheduling
public class ApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
