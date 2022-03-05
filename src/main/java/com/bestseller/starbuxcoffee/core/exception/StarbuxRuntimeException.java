package com.bestseller.starbuxcoffee.core.exception;

public class StarbuxRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3418591843597747899L;

	public StarbuxRuntimeException() {
		super();
	}

	public StarbuxRuntimeException(final String message) {
		super(message);
	}

	public StarbuxRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public StarbuxRuntimeException(final Throwable cause) {
		super(cause);
	}

}
