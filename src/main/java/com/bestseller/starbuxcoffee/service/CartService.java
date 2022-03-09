package com.bestseller.starbuxcoffee.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.ComboDTO;
import com.bestseller.starbuxcoffee.dto.CustomerInfoDTO;
import com.bestseller.starbuxcoffee.model.Cart;
import com.bestseller.starbuxcoffee.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository repository;

	@Autowired
	PriceService priceService;

	@Autowired
	OrderService orderService;

	public CartDTO openCart(final CustomerInfoDTO customerInfoDTO) {
		this.validateCustomerInfo(customerInfoDTO);

		final Cart cart = new Cart();
		cart.setCustomerId(customerInfoDTO.getCustomerId());
		cart.setExpiresAt(LocalDateTime.now().plusMinutes(20));
		this.repository.saveAndFlush(cart);

		return this.orderService.populateCart(cart);

	}

	public CartDTO getCartDTO(final String cartId) {
		return this.orderService.populateCart(this.getCart(cartId));
	}

	public void validateClientCartId(final String cartId) {
		this.getCart(cartId);
	}

	private Cart getCart(final String cartId) {
		final Optional<Cart> cart = this.repository.findById(UUID.fromString(cartId));

		if (cart.isPresent()) {
			if (cart.get().getExpiresAt().isBefore(LocalDateTime.now())) {
				throw new BusinessException("Cart is expired. Open a new Cart.");
			}

			if (cart.get().getCheckoutAt() != null) {
				throw new BusinessException("Cart is already closed.");
			}
			return cart.get();
		}

		throw new BusinessException("Cart not found.");

	}

	public CartDTO addItem(final String cartId, final ComboDTO comboDTO) {
		if (comboDTO == null //
				|| comboDTO.getPrincipalComboItem() == null //
				|| comboDTO.getPrincipalComboItem().getProduct().getPriceId() == 0) {
			throw new BusinessException("Product is invalid");
		}
		return this.orderService.addItemToOrder(this.getCart(cartId), comboDTO);
	}

	public CartDTO syncCart(final String cartId, final CartDTO cartDTO) {
		if (cartDTO == null //
				|| cartDTO.getOrder() == null) {
			throw new BusinessException("Invalid payload");
		}
		return this.orderService.syncCartOrder(this.getCart(cartId), cartDTO);
	}

	private void validateCustomerInfo(final CustomerInfoDTO customerInfoDTO) {
		if (customerInfoDTO == null) {
			throw new BusinessException("Customer Info is not valid");
		}

		if (StringUtils.isEmpty(customerInfoDTO.getCustomerId())) {
			throw new BusinessException("Customer Id is not valid");
		}

	}

	public CartDTO checkout(final String cartId, final CustomerInfoDTO customerInfo) {
		this.validateCustomerInfo(customerInfo);
		final Cart cart = this.getCart(cartId);

		this.checkInfos(customerInfo, cart);
		final CartDTO dto = this.orderService.populateCart(cart);
		cart.checkout();
		this.repository.save(cart);

		return dto;
	}

	private void checkInfos(final CustomerInfoDTO customerInfo, final Cart cart) {
		if (!StringUtils.equalsIgnoreCase(customerInfo.getCustomerId(), cart.getCustomerId())) {
			throw new BusinessException("Your customer id is invalid. It's impossible to checkout");
		}
	}

}
