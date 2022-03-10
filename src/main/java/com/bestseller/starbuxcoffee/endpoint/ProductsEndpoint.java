package com.bestseller.starbuxcoffee.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.dto.ProductDTO;
import com.bestseller.starbuxcoffee.dto.ProductsDTO;
import com.bestseller.starbuxcoffee.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/products")
@Tag(name = "3. Products Endpoint")
public class ProductsEndpoint {

	@Autowired
	ProductService service;

	@GetMapping
	@Operation(summary = "Get all products", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<ProductsDTO> products() {
		return ResponseEntity.ok(this.service.getAllValidProducts());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by ID", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<ProductDTO> products(@PathVariable("id") final int id) {
		return ResponseEntity.ok(this.service.getById(id));
	}

}
