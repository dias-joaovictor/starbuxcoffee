package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductsDTO implements Serializable {

	private static final long serialVersionUID = -7099901369872864777L;

	private List<ProductDTO> products;

	public ProductsDTO(final List<ProductDTO> products) {
		super();
		this.products = products;
	}

	public ProductsDTO() {
		super();
		this.products = new ArrayList<>();
	}

	public List<ProductDTO> getProducts() {
		return this.products;
	}

	public void setProducts(final List<ProductDTO> products) {
		this.products = products;
	}

}
