package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AmountPerOrderResponse implements Serializable {

	private static final long serialVersionUID = 5163439126359053947L;

	private List<AmountPerOrderDTO> data;

	public AmountPerOrderResponse(final List<AmountPerOrderDTO> data) {
		super();
		this.data = data;
	}

	public AmountPerOrderResponse() {
		super();
		this.data = new ArrayList<>();
	}

	public List<AmountPerOrderDTO> getData() {
		return this.data;
	}

	public void setData(final List<AmountPerOrderDTO> data) {
		this.data = data;
	}

}
