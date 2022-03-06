package com.bestseller.starbuxcoffee.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.CustomerInfoDTO;
import com.bestseller.starbuxcoffee.model.Cart;
import com.bestseller.starbuxcoffee.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository repository;

	public CartDTO openCart(final CustomerInfoDTO customerInfoDTO) {
		this.validateCustomerInfo(customerInfoDTO);

		final Cart cart = new Cart();
		cart.setCustomerId(new BCryptPasswordEncoder(10).encode(customerInfoDTO.getCustomerId()));
		cart.setExpiresAt(LocalDateTime.now().plusMinutes(20));

		this.repository.save(cart);

		return cart.toDTO();

	}

	public CartDTO getCart(final String cartId) {
		final Optional<Cart> cart = this.repository.findById(UUID.fromString(cartId));

		if (cart.isPresent()) {
			if (cart.get().getExpiresAt().isBefore(LocalDateTime.now())) {
				throw new BusinessException("Cart is expired. Open a new Cart.");
			}
			return cart.get().toDTO();
		}

		throw new BusinessException("Cart not found.");

	}

	private void validateCustomerInfo(final CustomerInfoDTO customerInfoDTO) {
		if (customerInfoDTO == null) {
			throw new BusinessException("Customer Info is not valid");
		}

		if (StringUtils.isEmpty(customerInfoDTO.getCustomerId())) {
			throw new BusinessException("Customer Id is not valid");
		}

	}

}
