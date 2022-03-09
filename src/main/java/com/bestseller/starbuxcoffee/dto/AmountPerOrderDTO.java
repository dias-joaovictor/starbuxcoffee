package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AmountPerOrderDTO implements Serializable {

	private static final long serialVersionUID = 2208086083217445688L;

	private String orderId;

	private LocalDateTime checkoutTime;

	private double amount;

	public AmountPerOrderDTO() {
		super();
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(final double amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(final String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getCheckoutTime() {
		return this.checkoutTime;
	}

	public void setCheckoutTime(final LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

}
