package com.bestseller.starbuxcoffee.security.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestseller.starbuxcoffee.security.dto.LoginDTO;
import com.bestseller.starbuxcoffee.security.dto.TokenDTO;
import com.bestseller.starbuxcoffee.security.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthEndpoint {

	@Autowired
	private AuthService authService;

	@PostMapping
	public ResponseEntity<TokenDTO> auth(@RequestBody @Validated final LoginDTO loginDTO) {
		return ResponseEntity.ok(this.authService.auth(loginDTO));

	}

	@GetMapping("/test/{param}")
	public String testPass(@PathVariable final String param) {
		final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(10);

		return bcrypt.encode(param);
	}

}
