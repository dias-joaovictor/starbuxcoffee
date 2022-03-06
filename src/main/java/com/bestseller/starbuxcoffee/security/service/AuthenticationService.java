package com.bestseller.starbuxcoffee.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.security.model.User;
import com.bestseller.starbuxcoffee.security.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Optional<User> optional = this.repository.findByEmail(username);

		if (optional.isPresent()) {
			return optional.get();
		}

		throw new UsernameNotFoundException("User not found");
	}

}