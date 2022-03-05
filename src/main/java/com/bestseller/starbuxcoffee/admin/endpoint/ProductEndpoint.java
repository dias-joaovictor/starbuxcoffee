package com.bestseller.starbuxcoffee.admin.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
public class ProductEndpoint {

	@GetMapping
	public ResponseEntity<String> test() {
		return ResponseEntity.ok().build();
	}
}
