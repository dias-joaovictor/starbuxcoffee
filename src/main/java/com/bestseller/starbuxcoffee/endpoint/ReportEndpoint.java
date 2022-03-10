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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/reports")
@Tag(name = "5. Reports Endpoint")
public class ReportEndpoint {

	@Autowired
	ReportService service;

	@GetMapping("/mostusedtoppings")
	@Operation(summary = "Most used toppings", //
			security = { @SecurityRequirement(name = "bearer-key") }, responses = { //
					@ApiResponse(responseCode = "200"), //
					@ApiResponse(responseCode = "404", content = @Content), //
					@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<MostUsedToppingsForDrinkResponse> getMostUsedToppings() {
		return ResponseEntity.ok(this.service.getMostUsedToppingsForDrinkReport());
	}

	@GetMapping("/amountorderpercustomer")
	@Operation(summary = "Amount order per customer", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<AmountOrderPerCustomerResponse> getAmountOrderPerCustomer() {
		return ResponseEntity.ok(this.service.getAmountOrderPerCustomerReport());
	}

	@GetMapping("/amountperorder")
	@Operation(summary = "Amount per order", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<AmountPerOrderResponse> getAmountPerOrder() {
		return ResponseEntity.ok(this.service.getAmountPerOrderReport());
	}

}
