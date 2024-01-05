package com.artiexh.api.base.response;

import java.time.Instant;

public record ResponseModel(
	Instant timestamp,
	String code,
	int status,
	String name,
	String message,
	String path,
	Object data
) {
}
