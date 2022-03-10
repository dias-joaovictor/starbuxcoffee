package com.bestseller.starbuxcoffee.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.ComboDTO;
import com.bestseller.starbuxcoffee.dto.CustomerInfoDTO;
import com.bestseller.starbuxcoffee.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@Tag(name = "4. Cart Endpoint")
public class CartEndpoint {

	@Autowired
	CartService cartService;

	@GetMapping("/{cartId}")
	@Operation(summary = "Get current cart", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<CartDTO> getCart(//
			@PathVariable("cartId") final String cartId) {
		return ResponseEntity.ok(this.cartService.getCartDTO(cartId));
	}

	@PostMapping("/open")
	@Operation(summary = "Open a new cart", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<CartDTO> openCart(//
			@RequestBody final CustomerInfoDTO customerInfo) {
		return ResponseEntity.ok(this.cartService.openCart(customerInfo));
	}

	@PutMapping("/{cartId}/item")
	@Operation(summary = "Add new item to cart", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<CartDTO> addItemToCart(//
			@PathVariable("cartId") final String cartId, //
			@RequestBody final ComboDTO comboDTO) {
		return ResponseEntity.ok(this.cartService.addItem(cartId, comboDTO));
	}

	@PutMapping("/{cartId}/sync")
	@Operation(summary = "Sync cart", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<CartDTO> syncCart(//
			@PathVariable("cartId") final String cartId, //
			@RequestBody final CartDTO cartDTO) {
		return ResponseEntity.ok(this.cartService.syncCart(cartId, cartDTO));
	}

	@PostMapping("/{cartId}/checkout")
	@Operation(summary = "Checkout", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "404", content = @Content), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<CartDTO> addItemToCart(//
			@PathVariable("cartId") final String cartId, //
			@RequestBody final CustomerInfoDTO customerInfo) {
		return ResponseEntity.ok(this.cartService.checkout(cartId, customerInfo));
	}

}
