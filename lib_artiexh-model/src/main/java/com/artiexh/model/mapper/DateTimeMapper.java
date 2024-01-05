package com.artiexh.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface DateTimeMapper {
	@Named("fromUTCToLocal")
	default Instant fromUTCToLocal(LocalDateTime localDateTime) {
		return localDateTime == null
			? null
			: localDateTime.atZone(ZoneId.of("UTC"))
			//.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"))
			.toInstant();
	}
}
