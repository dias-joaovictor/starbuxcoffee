package com.bestseller.starbuxcoffee.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComboDTO implements Serializable {

	private static final long serialVersionUID = -7369244693778832267L;

	private ComboItemDTO principalComboItem;

	private List<ComboItemDTO> sideComboItens;

	public ComboDTO() {
		super();
		this.sideComboItens = new ArrayList<>();
	}

	public ComboItemDTO getPrincipalComboItem() {
		return this.principalComboItem;
	}

	public void setPrincipalComboItem(final ComboItemDTO principalComboItem) {
		this.principalComboItem = principalComboItem;
	}

	public List<ComboItemDTO> getSideComboItens() {
		return this.sideComboItens;
	}

	public void setSideComboItens(final List<ComboItemDTO> sideComboItens) {
		this.sideComboItens = sideComboItens;
	}

}
