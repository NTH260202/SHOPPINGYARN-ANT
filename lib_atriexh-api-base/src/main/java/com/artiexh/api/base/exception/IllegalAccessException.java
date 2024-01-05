package com.artiexh.api.base.exception;

public class IllegalAccessException extends RuntimeException {
	public IllegalAccessException() {
		super();
	}

	public IllegalAccessException(String s) {
		super(s);
	}

	public IllegalAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalAccessException(Throwable cause) {
		super(cause);
	}
}
