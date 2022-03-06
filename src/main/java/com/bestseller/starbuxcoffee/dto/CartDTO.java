package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CartDTO implements Serializable {

	private static final long serialVersionUID = -1486219164261337790L;

	private String cartId;

	private LocalDateTime expiresAt;

	private long remainingTime;

	public CartDTO() {
		super();
	}

	public String getCartId() {
		return this.cartId;
	}

	public void setCartId(final String cartId) {
		this.cartId = cartId;
	}

	public LocalDateTime getExpiresAt() {
		return this.expiresAt;
	}

	public void setExpiresAt(final LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public long getRemainingTime() {
		return this.remainingTime;
	}

	public void setRemainingTime(final long remainingTime) {
		this.remainingTime = remainingTime;
	}

}
