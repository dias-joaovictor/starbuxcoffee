package com.bestseller.starbuxcoffee.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/admin/products")
@Tag(name = "2. Admin Products Endpoint")
public class AdminProductsEndpoint {

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

	@PostMapping
	@Operation(summary = "Create a product", responses = { //
			@ApiResponse(responseCode = "201"), //
			@ApiResponse(responseCode = "404"), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<Void> create(@RequestBody final ProductDTO productDTO) {
		this.service.createProduct(productDTO);
		return ResponseEntity.status(201).build();
	}

	@PutMapping
	@Operation(summary = "Update a product", responses = { //
			@ApiResponse(responseCode = "204"), //
			@ApiResponse(responseCode = "404"), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<Void> update(@RequestBody final ProductDTO productDTO) {
		this.service.updateProduct(productDTO);
		return ResponseEntity.status(204).build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a product by ID", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") final int id) {
		this.service.deleteProduct(id);
		return ResponseEntity.ok(null);
	}
}
