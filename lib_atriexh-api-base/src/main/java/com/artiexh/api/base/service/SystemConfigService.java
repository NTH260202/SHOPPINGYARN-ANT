package com.artiexh.api.base.service;

import com.artiexh.data.jpa.entity.SystemConfigEntity;
import com.artiexh.model.domain.SystemConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.function.Supplier;

public interface SystemConfigService {

	String get(String key);

	String getOrDefault(String key, String defaultValue);

	<X extends Throwable> String getOrThrow(String key, Supplier<? extends X> exceptionSupplier) throws X;

	SystemConfigEntity set(String key, String value);

	SystemConfigEntity set(String key, String value, String updateBy);

	void reload();

	Page<SystemConfig> getAll(String keyword, Pageable pageable);

	String delete(String key);

}
