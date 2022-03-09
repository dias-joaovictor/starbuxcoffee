package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MostUsedToppingsForDrinkResponse implements Serializable {

	private static final long serialVersionUID = -5260840961503080799L;

	private List<MostUsedToppingsForDrinkDTO> data;

	public MostUsedToppingsForDrinkResponse(final List<MostUsedToppingsForDrinkDTO> data) {
		super();
		this.data = data;
	}

	public MostUsedToppingsForDrinkResponse() {
		super();
		this.data = new ArrayList<>();
	}

	public List<MostUsedToppingsForDrinkDTO> getData() {
		return this.data;
	}

	public void setData(final List<MostUsedToppingsForDrinkDTO> data) {
		this.data = data;
	}

}
