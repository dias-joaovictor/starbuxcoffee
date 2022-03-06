package com.bestseller.starbuxcoffee.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.dto.AmountOrderPerCustomerDTO;
import com.bestseller.starbuxcoffee.dto.AmountPerOrderDTO;
import com.bestseller.starbuxcoffee.dto.MostUsedToppingsForDrinkDTO;
import com.bestseller.starbuxcoffee.helper.AmountOrderPerCustomerRowMapper;
import com.bestseller.starbuxcoffee.helper.AmountPerOrderRowMapper;
import com.bestseller.starbuxcoffee.helper.MostUsedToppingsForDrinkRowMapper;

@Repository
public class ReportNativeRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<AmountOrderPerCustomerDTO> getAmountOrderPerCustomer() {
		final String query = "select  " //
				+ "	STB_CART.customerId as customerId, " //
				+ "	sum(finalPrice) as amount " //
				+ "from STB_ORDER join STB_CART " //
				+ "	on STB_ORDER.cartId = STB_CART.id " //
				+ "where STB_CART.checkoutAt is not null " //
				+ "group by STB_CART.customerId ";

		return this.jdbcTemplate.query(query, new AmountOrderPerCustomerRowMapper());
	}

	public List<AmountPerOrderDTO> getAmountPerOrder() {
		final String query = "select  " //
				+ "STB_ORDER.id as orderId, " //
				+ "    STB_CART.checkoutAt as checkoutAt, " //
				+ "	finalPrice as finalPrice " //
				+ "from STB_ORDER join STB_CART " //
				+ "	on STB_ORDER.cartId = STB_CART.id " //
				+ "where STB_CART.checkoutAt is not null "//
				+ "order by STB_CART.checkoutAt desc";

		return this.jdbcTemplate.query(query, new AmountPerOrderRowMapper());
	}

	public List<MostUsedToppingsForDrinkDTO> getMostUsedToppingForDrinks() {
		final String query = "select  " //
				+ "	drinkProduct.name as drinkName,  " //
				+ "    toppingProduct.name as toppingName, " //
				+ "    count('*') as count " //
				+ "from STB_ORDER join STB_CART " //
				+ "	on STB_ORDER.cartId = STB_CART.id " //
				+ "join STB_COMBO comboTopping " //
				+ "	on comboTopping.orderId = STB_ORDER.id " //
				+ "join STB_COMBO comboDrink " //
				+ "	on comboDrink.id = comboTopping.principalComboId " //
				+ "join STB_PRICE drinkPrice " //
				+ "	on drinkPrice.id = comboDrink.priceId " //
				+ "join STB_PRODUCT drinkProduct " //
				+ "	on drinkProduct.id = drinkPrice.productId " //
				+ "		and drinkProduct.type = 'DRINK' " //
				+ "join STB_PRICE toppingPrice " //
				+ "	on toppingPrice.id = comboTopping.priceId " //
				+ "join STB_PRODUCT toppingProduct " //
				+ "	on toppingProduct.id = toppingPrice.productId " //
				+ "		and toppingProduct.type = 'TOPPING' " //
				+ "where STB_CART.checkoutAt is not null " //
				+ "group by drinkProduct.name, toppingProduct.name ";

		return this.jdbcTemplate.query(query, new MostUsedToppingsForDrinkRowMapper());
	}

}
