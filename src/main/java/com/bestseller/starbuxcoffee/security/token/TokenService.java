package com.bestseller.starbuxcoffee.security.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.StarbuxRuntimeException;
import com.bestseller.starbuxcoffee.security.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${jwt.expiration.min}")
	private String expiration;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${application.name}")
	private String applicationName;

	public String generateToken(final Authentication authentication) {

		final User user = (User) authentication.getPrincipal();

		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime expires = now.plusMinutes(this.extractExpiration());

		return Jwts.builder()//
				.setIssuer(this.applicationName)//
				.setSubject(user.getId().toString())//
				.setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())) //
				.setExpiration(Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()))//
				.signWith(SignatureAlgorithm.HS256, this.secret)//
				.compact();
	}

	private long extractExpiration() {
		try {
			return Long.parseLong(this.expiration);
		} catch (final Exception e) {
			throw new StarbuxRuntimeException(
					"Couldn't extract the expiration time. Set the environment variable JWT_EXPIRATION");
		}

	}

	public boolean isTokenValid(final String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	public Integer getTokenId(final String token) {
		final Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Integer.valueOf(body.getSubject());
	}

}
