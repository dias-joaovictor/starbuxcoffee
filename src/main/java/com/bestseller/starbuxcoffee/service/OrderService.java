package com.bestseller.starbuxcoffee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.ComboDTO;
import com.bestseller.starbuxcoffee.dto.OrderDTO;
import com.bestseller.starbuxcoffee.model.Cart;
import com.bestseller.starbuxcoffee.model.Order;
import com.bestseller.starbuxcoffee.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ComboService comboService;

	@Autowired
	DiscountService discountService;

	public OrderDTO createOrderIfNecessary(final Cart cart) {

		final Order order = this.getOrder(cart);

		return order.toDTO();
	}

	private Order getOrder(final Cart cart) {
		return this.orderRepository.findByCart(cart).orElseGet(() -> {
			final Order orderItem = Order.create(cart);
			this.orderRepository.saveAndFlush(orderItem);
			return orderItem;
		});
	}

	public CartDTO populateCart(final Cart cart) {
		final CartDTO cartDTO = cart.toDTO();
		final Order order = this.getOrder(cart);
		cartDTO.setOrder(this.comboService.populateOrder(order));

		this.calculate(order, cartDTO.getOrder());
		this.orderRepository.saveAndFlush(order);
		return cartDTO;
	}

	private void calculate(final Order order, final OrderDTO orderDTO) {
		this.discountService.compute(order, orderDTO);
	}

	public CartDTO addItemToOrder(final Cart cart, final ComboDTO comboItemDTO) {
		final CartDTO cartDTO = this.populateCart(cart);
		final Order order = this.getOrder(cart);
		cartDTO.setOrder(this.comboService.addComboItemToOrder(order, comboItemDTO));
		this.calculate(order, cartDTO.getOrder());
		this.orderRepository.saveAndFlush(order);
		return cartDTO;
	}

	public CartDTO syncCartOrder(final Cart cart, final CartDTO cartDTO) {
		final CartDTO cartDTONew = this.populateCart(cart);
		final Order order = this.getOrder(cart);
		cartDTONew.setOrder(this.comboService.syncOrder(order, cartDTO.getOrder()));
		this.calculate(order, cartDTONew.getOrder());
		this.orderRepository.saveAndFlush(order);
		return cartDTO;
	}

}
