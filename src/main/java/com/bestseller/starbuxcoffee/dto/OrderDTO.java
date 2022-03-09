package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = -6476729485350817801L;

	private String id;

	private double totalPrice;

	private double finalPrice;

	private List<ComboDTO> combos;

	public OrderDTO() {
		super();
		this.combos = new ArrayList<>();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public List<ComboDTO> getCombos() {
		return this.combos;
	}

	public void setCombos(final List<ComboDTO> combos) {
		this.combos = combos;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(final double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getFinalPrice() {
		return this.finalPrice;
	}

	public void setFinalPrice(final double finalPrice) {
		this.finalPrice = finalPrice;
	}

}
