package com.bestseller.starbuxcoffee.core;

import java.math.BigDecimal;

public final class BasicMathOperations {

	private BasicMathOperations() {
		super();
	}

	public static double truncateDecimal(final double x, final int numberofDecimals) {
		if (x > 0) {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR).doubleValue();
		} else {
			return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING).doubleValue();
		}
	}

}
