package com.bestseller.starbuxcoffee.security.filter;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class StarbuxBaseFilter extends OncePerRequestFilter {

	private final ObjectMapper mapper;
	private final AntPathMatcher antPathMatcher;

	protected StarbuxBaseFilter() {
		this.antPathMatcher = new AntPathMatcher();
		this.mapper = new ObjectMapper()//
				.registerModule(new JavaTimeModule()) //
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	protected ObjectMapper getMapper() {
		return this.mapper;
	}

	protected AntPathMatcher getAntPathMatcher() {
		return this.antPathMatcher;
	}

}
