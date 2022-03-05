package com.bestseller.starbuxcoffee.core.exception;

import java.time.LocalDateTime;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bestseller.starbuxcoffee.core.ExceptionResponse;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllException(final Exception ex, final WebRequest request) {
		return this.getResponseWithStatusCode(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<Object> handleUnauthorizedException(final Exception ex, final WebRequest request) {
		return this.getResponseWithStatusCode(ex, request, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({ StarbuxRuntimeException.class, RuntimeException.class, BusinessException.class })
	public final ResponseEntity<Object> handleStarbuxRuntimeException(final Exception ex, final WebRequest request) {
		return this.getResponseWithStatusCode(ex, request, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> getResponseWithStatusCode(final Exception ex, final WebRequest request,
			final HttpStatus status) {
		return new ResponseEntity<>(this.getResponse(ex, request), status);
	}

	private ExceptionResponse getResponse(final Exception ex, final WebRequest request) {
		return new ExceptionResponse(//
				LocalDateTime.now(), //
				ex.getMessage(), //
				request.getDescription(false), //
				ExceptionUtils.getStackTrace(ex));
	}

}
