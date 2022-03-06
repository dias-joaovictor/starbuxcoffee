package com.bestseller.starbuxcoffee.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bestseller.starbuxcoffee.core.BasicMathOperations;
import com.bestseller.starbuxcoffee.dto.AmountPerOrderDTO;

public class AmountPerOrderRowMapper implements RowMapper<AmountPerOrderDTO> {

	@Override
	public AmountPerOrderDTO mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		final AmountPerOrderDTO dto = new AmountPerOrderDTO();
		dto.setOrderId(rs.getString("orderId"));
		dto.setCheckoutTime(rs.getTimestamp("checkoutAt").toLocalDateTime());
		dto.setAmount(BasicMathOperations.truncateDecimal(rs.getDouble("finalPrice"), 2));
		return dto;
	}

}
