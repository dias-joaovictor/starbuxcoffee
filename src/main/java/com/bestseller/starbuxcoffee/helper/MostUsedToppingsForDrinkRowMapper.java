package com.bestseller.starbuxcoffee.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bestseller.starbuxcoffee.dto.MostUsedToppingsForDrinkDTO;

public class MostUsedToppingsForDrinkRowMapper implements RowMapper<MostUsedToppingsForDrinkDTO> {

	@Override
	public MostUsedToppingsForDrinkDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final MostUsedToppingsForDrinkDTO dto = new MostUsedToppingsForDrinkDTO();
		dto.setCount(rs.getInt("count"));
		dto.setDrink(rs.getString("drinkName"));
		dto.setTopping(rs.getString("toppingName"));
		return dto;
	}

}
