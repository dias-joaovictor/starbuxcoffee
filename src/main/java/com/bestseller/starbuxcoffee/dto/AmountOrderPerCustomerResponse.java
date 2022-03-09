package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AmountOrderPerCustomerResponse implements Serializable {

	private static final long serialVersionUID = 5588813825545005074L;

	private List<AmountOrderPerCustomerDTO> data;

	public AmountOrderPerCustomerResponse(final List<AmountOrderPerCustomerDTO> data) {
		super();
		this.data = data;
	}

	public AmountOrderPerCustomerResponse() {
		super();
		this.data = new ArrayList<>();
	}

	public List<AmountOrderPerCustomerDTO> getData() {
		return this.data;
	}

	public void setData(final List<AmountOrderPerCustomerDTO> data) {
		this.data = data;
	}

}
