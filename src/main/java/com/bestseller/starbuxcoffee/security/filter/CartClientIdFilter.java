package com.bestseller.starbuxcoffee.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import com.bestseller.starbuxcoffee.core.ExceptionResponse;
import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.service.CartService;

public class CartClientIdFilter extends StarbuxBaseFilter {

	private static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12})");

	private final List<String> shouldFilter;
	private final CartService cartService;

	public CartClientIdFilter(final List<String> shouldFilter, final CartService cartService) {
		super();
		this.shouldFilter = shouldFilter;
		this.cartService = cartService;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {

		try {
			this.validateCartClient(request.getServletPath());
			filterChain.doFilter(request, response);
		} catch (final Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(this.getMapper().writeValueAsString(new ExceptionResponse(//
					LocalDateTime.now(), //
					"Invalid CartClientId", //
					e.getMessage(), //
					ExceptionUtils.getStackTrace(e))));
			response.setContentType("application/json");
		}

	}

	private void validateCartClient(final String servletPath) {

		final Matcher matcher = UUID_PATTERN.matcher(servletPath);
		String uuid = null;
		if (matcher.find()) {
			uuid = matcher.group();
		}

		if (StringUtils.isEmpty(uuid)) {
			throw new BusinessException("CartClientId is invalid.");
		}

		this.cartService.validateClientCartId(uuid);
	}

	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
		return this.shouldFilter.stream().noneMatch(p -> this.getAntPathMatcher().match(p, request.getServletPath()))
				|| request.getServletPath().contains("/open");
	}

}
