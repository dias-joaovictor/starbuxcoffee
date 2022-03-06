package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;

public class CustomerInfoDTO implements Serializable {

	private static final long serialVersionUID = 2504098681197574994L;

	private String customerId;

	public CustomerInfoDTO() {
		super();
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(final String customerId) {
		this.customerId = customerId;
	}

}
