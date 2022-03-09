package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;

public class MostUsedToppingsForDrinkDTO implements Serializable {

	private static final long serialVersionUID = -3280406129027035782L;

	private String drink;

	private String topping;

	private int count;

	public MostUsedToppingsForDrinkDTO() {
		super();
	}

	public String getDrink() {
		return this.drink;
	}

	public void setDrink(final String drink) {
		this.drink = drink;
	}

	public String getTopping() {
		return this.topping;
	}

	public void setTopping(final String topping) {
		this.topping = topping;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(final int count) {
		this.count = count;
	}

}
