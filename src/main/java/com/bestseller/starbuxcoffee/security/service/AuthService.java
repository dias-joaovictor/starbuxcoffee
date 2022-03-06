package com.bestseller.starbuxcoffee.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.core.exception.UnauthorizedException;
import com.bestseller.starbuxcoffee.security.dto.LoginDTO;
import com.bestseller.starbuxcoffee.security.dto.TokenDTO;
import com.bestseller.starbuxcoffee.security.token.TokenService;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public TokenDTO auth(final LoginDTO loginDTO) {

		this.validate(loginDTO);

		final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getLogin(), loginDTO.getPassword());

		try {
			final Authentication authentication = this.authenticationManager
					.authenticate(usernamePasswordAuthenticationToken);

			final String token = this.tokenService.generateToken(authentication);

			return new TokenDTO("Bearer", token);
		} catch (final Exception e) {
			throw new UnauthorizedException("User not authorized", e);
		}

	}

	private void validate(final LoginDTO loginDTO) {
		if (loginDTO == null) {
			throw new BusinessException("Object is invalid");
		}

		if (!StringUtils.hasLength(loginDTO.getLogin())) {
			throw new BusinessException("Login is invalid");
		}

		if (!StringUtils.hasLength(loginDTO.getPassword())) {
			throw new BusinessException("Password is invalid");
		}

	}

}
