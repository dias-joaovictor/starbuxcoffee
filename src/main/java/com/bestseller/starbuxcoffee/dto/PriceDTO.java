package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class PriceDTO implements Serializable {

	private static final long serialVersionUID = 5677755592808843470L;

	private int id;

	private double value;

	private LocalDate startValidDate;

	private LocalDate expirationDate;

	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

	public LocalDate getStartValidDate() {
		return this.startValidDate;
	}

	public void setStartValidDate(final LocalDate startValidDate) {
		this.startValidDate = startValidDate;
	}

	public LocalDate getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(final LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
