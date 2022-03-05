package com.bestseller.starbuxcoffee.security.dto;

import java.io.Serializable;

public class TokenDTO implements Serializable {

	private static final long serialVersionUID = 1340996201022553642L;

	private String type;

	private String token;

	public TokenDTO() {
		super();
	}

	public TokenDTO(final String type, final String token) {
		super();
		this.type = type;
		this.token = token;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

}