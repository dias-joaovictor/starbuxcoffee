package com.bestseller.starbuxcoffee.security.dto;

import java.io.Serializable;

public class LoginDTO implements Serializable {

	private static final long serialVersionUID = 5209693313198349600L;

	private String login;

	private String password;

	public LoginDTO() {
		super();
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
