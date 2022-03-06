package com.bestseller.starbuxcoffee.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.dto.AmountOrderPerCustomerResponse;
import com.bestseller.starbuxcoffee.dto.AmountPerOrderResponse;
import com.bestseller.starbuxcoffee.dto.MostUsedToppingsForDrinkResponse;
import com.bestseller.starbuxcoffee.service.ReportService;

@RestController
@RequestMapping("/admin/reports")
public class ReportEndpoint {

	@Autowired
	ReportService service;

	@GetMapping("/mostusedtoppings")
	public ResponseEntity<MostUsedToppingsForDrinkResponse> getMostUsedToppings() {
		return ResponseEntity.ok(this.service.getMostUsedToppingsForDrinkReport());
	}

	@GetMapping("/amountorderpercustomer")
	public ResponseEntity<AmountOrderPerCustomerResponse> getAmountOrderPerCustomer() {
		return ResponseEntity.ok(this.service.getAmountOrderPerCustomerReport());
	}

	@GetMapping("/amountperorder")
	public ResponseEntity<AmountPerOrderResponse> getAmountPerOrder() {
		return ResponseEntity.ok(this.service.getAmountPerOrderReport());
	}

}
