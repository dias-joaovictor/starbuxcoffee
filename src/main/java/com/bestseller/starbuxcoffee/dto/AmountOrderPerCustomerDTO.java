package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;

public class AmountOrderPerCustomerDTO implements Serializable {

	private static final long serialVersionUID = -8442022712413175106L;

	private String customerId;

	private double amount;

	public AmountOrderPerCustomerDTO() {
		super();
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(final String customerId) {
		this.customerId = customerId;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

}
