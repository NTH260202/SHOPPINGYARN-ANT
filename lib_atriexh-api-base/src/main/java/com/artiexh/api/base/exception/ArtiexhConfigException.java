package com.artiexh.api.base.exception;

public class ArtiexhConfigException extends RuntimeException {
	public ArtiexhConfigException() {
		super();
	}

	public ArtiexhConfigException(String message) {
		super(message);
	}

	public ArtiexhConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArtiexhConfigException(Throwable cause) {
		super(cause);
	}

	protected ArtiexhConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
