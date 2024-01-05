package com.artiexh.api.base.exception;

import lombok.Getter;

@Getter
public class InvalidException extends IllegalArgumentException {
	private final ErrorCode errorCode;
	public InvalidException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public InvalidException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
