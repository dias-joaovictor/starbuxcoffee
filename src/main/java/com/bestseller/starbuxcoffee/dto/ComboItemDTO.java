package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;

public class ComboItemDTO implements Serializable {

	private static final long serialVersionUID = 65897821255187706L;

	private String id;

	private ProductDTO product;

	public ComboItemDTO() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public ProductDTO getProduct() {
		return this.product;
	}

	public void setProduct(final ProductDTO product) {
		this.product = product;
	}

}
