package com.bestseller.starbuxcoffee.security.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.security.dto.LoginDTO;
import com.bestseller.starbuxcoffee.security.dto.TokenDTO;
import com.bestseller.starbuxcoffee.security.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "1. Authentication Endpoint")
public class AuthEndpoint {

	@Autowired
	private AuthService authService;

	@PostMapping
	@Operation(summary = "Authenticate", responses = { //
			@ApiResponse(responseCode = "200"), //
			@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))) })
	public ResponseEntity<TokenDTO> auth(@RequestBody @Validated final LoginDTO loginDTO) {
		return ResponseEntity.ok(this.authService.auth(loginDTO));

	}

}
