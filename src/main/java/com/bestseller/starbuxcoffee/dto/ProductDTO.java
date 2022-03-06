package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;

import com.bestseller.starbuxcoffee.model.Price;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = -5140701190286864359L;

	private int id;

	private int priceId;

	private String productType;

	private String name;

	private String description;

	private int priority;

	private double price;

	@JsonIgnore
	private Price priceItem;

	public ProductDTO() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(final String productType) {
		this.productType = productType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	public int getPriceId() {
		return this.priceId;
	}

	public void setPriceId(final int priceId) {
		this.priceId = priceId;
	}

	@JsonIgnore
	public void setPriceItem(final Price priceItem) {
		this.priceItem = priceItem;
	}

	@JsonIgnore
	public Price getPriceItem() {
		return this.priceItem;
	}

}
