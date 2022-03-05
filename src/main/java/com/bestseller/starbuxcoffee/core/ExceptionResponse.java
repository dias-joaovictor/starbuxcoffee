package com.bestseller.starbuxcoffee.core;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExceptionResponse implements Serializable {

	private static final long serialVersionUID = 8112082247408556512L;
	private LocalDateTime timestamp;
	private String message;
	private String details;
	private String trace;

	public ExceptionResponse() {
		super();
	}

	public ExceptionResponse(final LocalDateTime timestamp, final String message, final String details,
			final String trace) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
		this.trace = trace;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public String getTrace() {
		return this.trace;
	}

	public void setTrace(final String trace) {
		this.trace = trace;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [timestamp=" + this.timestamp + ", message=" + this.message + ", details="
				+ this.details + ", trace=" + this.trace + "]";
	}

}
