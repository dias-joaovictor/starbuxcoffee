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

@RestController
@RequestMapping("/admin/products")
public class AdminProductsEndpoint {

	@Autowired
	ProductService service;

	@GetMapping
	public ResponseEntity<ProductsDTO> products() {
		return ResponseEntity.ok(this.service.getAllValidProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> products(@PathVariable("id") final int id) {
		return ResponseEntity.ok(this.service.getById(id));
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody final ProductDTO productDTO) {
		this.service.createProduct(productDTO);
		return ResponseEntity.status(201).build();
	}

	@PutMapping
	public ResponseEntity<Void> update(@RequestBody final ProductDTO productDTO) {
		this.service.updateProduct(productDTO);
		return ResponseEntity.status(204).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") final int id) {
		this.service.deleteProduct(id);
		return ResponseEntity.ok(null);
	}
}
