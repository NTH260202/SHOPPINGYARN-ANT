package com.artiexh.api.base.utils;

import com.artiexh.api.base.common.Const;
import com.artiexh.api.base.exception.ArtiexhConfigException;
import com.artiexh.api.base.service.SystemConfigService;
import com.artiexh.data.jpa.entity.WardEntity;
import com.artiexh.data.jpa.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class SystemConfigHelper {
	private final SystemConfigService systemConfigService;
	private final WardRepository wardRepository;

	@Transactional(readOnly = true)
	public WardEntity getArtiexhWardEntity() {
		int wardId;
		try {
			wardId = Integer.parseInt(systemConfigService.get(Const.SystemConfigKey.ARTIEXH_PICK_WARD_ID));
		} catch (NumberFormatException ex) {
			log.warn("WardId is not a number");
			throw new ArtiexhConfigException("WardId is not a number");
		}

		return wardRepository.findById(wardId)
			.orElseThrow(() -> {
				log.warn("WardId not existed");
				return new ArtiexhConfigException("WardId not existed");
			});
	}

	public Duration getDuration(String key, long defaultMillis) {
		try {
			String value = systemConfigService.get(key);
			if (value == null) {
				log.warn("Duration is not configured, using default value");
				return Duration.ofMillis(defaultMillis);
			}
			long longValue = Long.parseLong(value);
			return Duration.ofMillis(longValue);
		} catch (Exception ex) {
			log.warn("Duration is not a valid format");
			return Duration.ofMillis(defaultMillis);
		}
	}

	public LocalTime getLocalTime(String key, LocalTime defaultValue) {
		try {
			String value = systemConfigService.get(key);
			if (value == null) {
				log.warn("LocalTime is not configured, using default value");
				return defaultValue;
			}
			return LocalTime.parse(value);
		} catch (Exception ex) {
			log.warn("LocalTime is not a valid format");
			return defaultValue;
		}
	}

}
