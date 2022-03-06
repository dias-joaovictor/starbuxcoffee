package com.bestseller.starbuxcoffee.security.token;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bestseller.starbuxcoffee.core.ExceptionResponse;
import com.bestseller.starbuxcoffee.security.model.User;
import com.bestseller.starbuxcoffee.security.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private final UserRepository userRepository;
	private final ObjectMapper mapper;
	private final List<String> bypassFilter;
	private final AntPathMatcher antPathMatcher;

	public TokenAuthenticationFilter(final TokenService tokenService, final UserRepository userRepository,
			final List<String> bypassFilter) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
		this.bypassFilter = bypassFilter;
		this.antPathMatcher = new AntPathMatcher();
		this.mapper = new ObjectMapper()//
				.registerModule(new JavaTimeModule()) //
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {

		final String tokenFromHeader = this.getTokenFromHeader(request);
		final boolean tokenValid = this.tokenService.isTokenValid(tokenFromHeader);
		if (tokenValid) {
			this.authenticate(tokenFromHeader);
			filterChain.doFilter(request, response);

		} else {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(this.mapper
					.writeValueAsString(new ExceptionResponse(LocalDateTime.now(), "Invalid Token.", null, null)));
			response.setContentType("application/json");
		}

	}

	@Override
	protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
		return this.bypassFilter.stream().anyMatch(p -> this.antPathMatcher.match(p, request.getServletPath()));
	}

	private void authenticate(final String tokenFromHeader) {
		final Integer id = this.tokenService.getTokenId(tokenFromHeader);

		final Optional<User> optionalUser = this.userRepository.findById(id);

		if (optionalUser.isPresent()) {

			final User user = optionalUser.get();

			final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					user, null, user.getProfiles());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}

	private String getTokenFromHeader(final HttpServletRequest request) {
		final String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
