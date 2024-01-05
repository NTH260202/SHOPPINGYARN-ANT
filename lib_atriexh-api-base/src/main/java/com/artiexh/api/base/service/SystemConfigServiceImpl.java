package com.artiexh.api.base.service;

import com.artiexh.data.jpa.entity.SystemConfigEntity;
import com.artiexh.data.jpa.repository.SystemConfigRepository;
import com.artiexh.model.domain.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

	private final ConcurrentMap<String, String> configs = new ConcurrentHashMap<>();
	private final SystemConfigRepository repository;

	@PostConstruct
	@Transactional(readOnly = true)
	public void init() {
		reload();
	}

	@Override
	public String get(String key) {
		return configs.get(key);
	}

	@Override
	public String getOrDefault(String key, String defaultValue) {
		return configs.getOrDefault(key, defaultValue);
	}

	@Override
	public <X extends Throwable> String getOrThrow(String key, Supplier<? extends X> exceptionSupplier) throws X {
		if (!configs.containsKey(key)) {
			throw exceptionSupplier.get();
		} else {
			return configs.get(key);
		}
	}

	@Override
	@Transactional
	public SystemConfigEntity set(String key, String value) {
		return set(key, value, null);
	}

	@Override
	@Transactional
	public SystemConfigEntity set(String key, String value, String updateBy) {
		SystemConfigEntity entity = new SystemConfigEntity(key, value, updateBy);
		var savedEntity = repository.save(entity);
		reload();
		return savedEntity;
	}

	@Override
	@Transactional(readOnly = true)
	public void reload() {
		configs.clear();
		configs.putAll(repository.findAll().stream().collect(Collectors.toMap(SystemConfigEntity::getKey, SystemConfigEntity::getValue)));
	}

	@Override
	public Page<SystemConfig> getAll(String keyword, Pageable pageable) {
		if (keyword != null) {
			return repository.findAllByKeyContains(keyword, pageable).map(entity -> new SystemConfig(entity.getKey(), entity.getValue(), entity.getUpdatedBy(), entity.getModifiedDate(), entity.getCreatedDate()));
		} else {
			return repository.findAll(pageable).map(entity -> new SystemConfig(entity.getKey(), entity.getValue(), entity.getUpdatedBy(), entity.getModifiedDate(), entity.getCreatedDate()));
		}
	}

	@Override
	@Transactional
	public String delete(String key) {
		var value = configs.get(key);
		if (value != null) {
			repository.deleteById(key);
			reload();
		}
		return value;
	}
}
