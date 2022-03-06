package com.bestseller.starbuxcoffee.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bestseller.starbuxcoffee.core.BasicMathOperations;
import com.bestseller.starbuxcoffee.dto.AmountOrderPerCustomerDTO;

public class AmountOrderPerCustomerRowMapper implements RowMapper<AmountOrderPerCustomerDTO> {

	@Override
	public AmountOrderPerCustomerDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final AmountOrderPerCustomerDTO dto = new AmountOrderPerCustomerDTO();
		dto.setAmount(BasicMathOperations.truncateDecimal(rs.getDouble("amount"), 2));
		dto.setCustomerId(rs.getString("customerId"));
		return dto;
	}

}
