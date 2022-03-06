package com.bestseller.starbuxcoffee.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.CustomerInfoDTO;
import com.bestseller.starbuxcoffee.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartEndpoint {

	@Autowired
	CartService cartService;

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDTO> getCart(//
			@PathVariable("cartId") final String cartId) {
		return ResponseEntity.ok(this.cartService.getCart(cartId));
	}

	@PostMapping("/open")
	public ResponseEntity<CartDTO> openCart(//
			@RequestBody final CustomerInfoDTO customerInfo) {
		return ResponseEntity.ok(this.cartService.openCart(customerInfo));
	}

}
