package com.bestseller.starbuxcoffee.core.exception;

public class BusinessException extends StarbuxRuntimeException {

	private static final long serialVersionUID = 5905932462959407334L;

	public BusinessException() {
		super();
	}

	public BusinessException(final String message) {
		super(message);
	}

	public BusinessException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BusinessException(final Throwable cause) {
		super(cause);
	}

}
