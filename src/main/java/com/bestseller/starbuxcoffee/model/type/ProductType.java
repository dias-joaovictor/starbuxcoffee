package com.bestseller.starbuxcoffee.model.type;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public enum ProductType {

	DRINK, //
	TOPPING;

	private static final String DRINK_TOPPING = "[DRINK, TOPPING]";

	public static final String getValidTypes() {
		return DRINK_TOPPING;
	}

	public static final ProductType fromName(final String productType) {
		return StringUtils.isEmpty(productType) ? null
				: Arrays.asList(values()).stream().filter(item -> productType.toUpperCase().equals(item.name()))
						.findAny().orElse(null);
	}

}
