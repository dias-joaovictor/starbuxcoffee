package com.bestseller.starbuxcoffee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.BasicMathOperations;
import com.bestseller.starbuxcoffee.dto.OrderDTO;
import com.bestseller.starbuxcoffee.model.Order;

@Service
public class DiscountService {

	public void compute(final Order order, final OrderDTO orderDTO) {

		final List<Double> itemValues = new ArrayList<>();

		this.computeTotalValue(order, orderDTO, itemValues);

		if (order.getTotalPrice() > 0) {
			final double logicOneCalculus = this.computeLogicOne(order, orderDTO);

			final double logicTwoCalculus = this.computeLogicTwo(order, orderDTO, itemValues);

			if (logicOneCalculus == 0d && logicTwoCalculus == 0d) {
				order.setFinalPrice(order.getTotalPrice());
			} else if (logicOneCalculus == 0d) {
				order.setFinalPrice(logicTwoCalculus);
			} else {
				order.setFinalPrice(logicOneCalculus);
			}
		}

		orderDTO.setFinalPrice(order.getFinalPrice());
		orderDTO.setTotalPrice(order.getTotalPrice());

	}

	private void computeTotalValue(final Order order, final OrderDTO orderDTO, final List<Double> itemValues) {
		order.setTotalPrice(0d);
		order.setFinalPrice(0d);

		final AtomicReference<Double> total = new AtomicReference<>(0d);
		if (this.isValidToCompute(orderDTO)) {
			orderDTO.getCombos().stream().filter(Objects::nonNull).forEach(combo -> {
				if (combo.getPrincipalComboItem() != null && combo.getPrincipalComboItem().getProduct() != null) {
					total.set(total.get() + combo.getPrincipalComboItem().getProduct().getPrice());
					itemValues.add(combo.getPrincipalComboItem().getProduct().getPrice());
				}

				if (combo.getSideComboItens() != null && !combo.getSideComboItens().isEmpty()) {
					combo.getSideComboItens().stream().filter(Objects::nonNull).forEach(sideComboItem -> {
						if (sideComboItem.getProduct() != null) {
							total.set(total.get() + sideComboItem.getProduct().getPrice());
							itemValues.add(sideComboItem.getProduct().getPrice());
						}
					});
				}
			});
		}

		order.setTotalPrice(BasicMathOperations.truncateDecimal(total.get(), 2));
		order.setFinalPrice(order.getTotalPrice());
	}

	private boolean isValidToCompute(final OrderDTO orderDTO) {
		return orderDTO != null && orderDTO.getCombos() != null && !orderDTO.getCombos().isEmpty();
	}

	private double computeLogicTwo(final Order order, final OrderDTO orderDTO, final List<Double> itemValues) {
		double value = 0;
		if (orderDTO.getCombos() != null //
				&& orderDTO.getCombos().size() >= 3 //
				&& itemValues != null //
				&& !itemValues.isEmpty()) {
			value = order.getTotalPrice() - itemValues.stream().mapToDouble(item -> item).min().orElse(0d);
			value = BasicMathOperations.truncateDecimal(value, 2);
		}
		return value;
	}

	private double computeLogicOne(final Order order, final OrderDTO orderDTO) {
		double value = 0;
		if (order.getTotalPrice() > 12d) {
			value = BasicMathOperations.truncateDecimal((order.getTotalPrice() * 0.75), 2);
		}
		return value;
	}

}
