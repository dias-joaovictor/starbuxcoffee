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

@RestController
@RequestMapping("/products")
public class ProductsEndpoint {

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

}
