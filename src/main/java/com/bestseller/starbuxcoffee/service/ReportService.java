package com.bestseller.starbuxcoffee.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.dto.AmountOrderPerCustomerResponse;
import com.bestseller.starbuxcoffee.dto.AmountPerOrderResponse;
import com.bestseller.starbuxcoffee.dto.MostUsedToppingsForDrinkDTO;
import com.bestseller.starbuxcoffee.dto.MostUsedToppingsForDrinkResponse;
import com.bestseller.starbuxcoffee.repository.ReportNativeRepository;

@Service
public class ReportService {

	@Autowired
	ReportNativeRepository reportNativeRepository;

	public AmountPerOrderResponse getAmountPerOrderReport() {
		return new AmountPerOrderResponse(this.reportNativeRepository.getAmountPerOrder());
	}

	public AmountOrderPerCustomerResponse getAmountOrderPerCustomerReport() {
		return new AmountOrderPerCustomerResponse(this.reportNativeRepository.getAmountOrderPerCustomer());
	}

	public MostUsedToppingsForDrinkResponse getMostUsedToppingsForDrinkReport() {

		final Map<String, MostUsedToppingsForDrinkDTO> map = new HashMap<>();
		this.reportNativeRepository.getMostUsedToppingForDrinks().stream().filter(Objects::nonNull).forEach(item -> {
			map.compute(item.getDrink(), (key, value) -> {

				if (value == null || value.getCount() < item.getCount()) {
					return item;
				}

				return value;
			});
		});

		return new MostUsedToppingsForDrinkResponse(map.values().stream().collect(Collectors.toList()));
	}

}
