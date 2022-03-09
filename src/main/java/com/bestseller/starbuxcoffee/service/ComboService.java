package com.bestseller.starbuxcoffee.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.dto.ComboDTO;
import com.bestseller.starbuxcoffee.dto.ComboItemDTO;
import com.bestseller.starbuxcoffee.dto.OrderDTO;
import com.bestseller.starbuxcoffee.model.Combo;
import com.bestseller.starbuxcoffee.model.Order;
import com.bestseller.starbuxcoffee.model.type.ProductType;
import com.bestseller.starbuxcoffee.repository.ComboRepository;

@Service
public class ComboService {

	@Autowired
	ComboRepository comboRepository;

	@Autowired
	PriceService priceService;

	public OrderDTO addComboItemToOrder(final Order order, final ComboDTO comboDTO) {
		this.validateAndPopulate(comboDTO);

		final Combo principalCombo = this.addPrincipalComboItem(order, comboDTO.getPrincipalComboItem());

		if (comboDTO.getSideComboItens() != null && !comboDTO.getSideComboItens().isEmpty()) {
			comboDTO.getSideComboItens().stream()
					.forEach(sideComboItem -> this.addSideComboItem(order, sideComboItem, principalCombo));
		}

		return this.populateOrder(order);
	}

	public OrderDTO populateOrder(final Order order) {
		final List<Combo> combos = this.getComboByOrder(order);

		final Map<String, ComboDTO> combosDTO = new HashMap<>();
		combos.stream().filter(combo -> combo.getPrincipalCombo() == null).forEach(combo -> {
			combosDTO.computeIfAbsent(combo.getId().toString(), key -> {
				final ComboDTO dto = new ComboDTO();
				dto.setPrincipalComboItem(this.getComboItemDTO(combo));
				return dto;
			});

		});

		combos.stream().filter(combo -> combo.getPrincipalCombo() != null).forEach(combo -> {

			combosDTO.computeIfPresent(combo.getPrincipalCombo().getId().toString(), (key, comboDTO) -> {
				comboDTO.getSideComboItens().add(this.getComboItemDTO(combo));
				return comboDTO;
			});

		});

		final OrderDTO orderDTO = order.toDTO();
		orderDTO.setCombos(combosDTO.values().stream().collect(Collectors.toList()));
		return orderDTO;
	}

	private List<Combo> getComboByOrder(final Order order) {
		final List<Combo> combos = this.comboRepository.findAllByOrderOrderByCreatedAt(order);
		Collections.sort(combos);
		return combos;
	}

	private ComboItemDTO getComboItemDTO(final Combo combo) {
		final ComboItemDTO dto = new ComboItemDTO();
		dto.setId(combo.getId().toString());
		dto.setProduct(combo.getPrice().toProductDTO());
		return dto;
	}

	private Combo addPrincipalComboItem(final Order order, final ComboItemDTO principalComboItem) {
		final Combo combo = this.getComboOrElseNew(order, principalComboItem);
		if (combo.getPrice().getProduct().getProductType() != ProductType.DRINK) {
			throw new BusinessException("Principal Combo Item Type is invalid. It must be a DRINK");
		}

		this.comboRepository.save(combo);
		return combo;
	}

	private Combo addSideComboItem(final Order order, final ComboItemDTO comboItemDTO, final Combo principalCombo) {
		final Combo combo = this.getComboOrElseNew(order, comboItemDTO);
		if (combo.getPrice().getProduct().getProductType() != ProductType.TOPPING) {
			throw new BusinessException("Side Combo Item Type is invalid. It must be a TOPPING");
		}

		if (combo.getId() == null) {
			combo.setPrincipalCombo(principalCombo);
			this.comboRepository.save(combo);
		}

		return combo;
	}

	private Combo getComboOrElseNew(final Order order, final ComboItemDTO principalComboItem) {
		Combo combo = null;
		try {
			combo = this.comboRepository.findByIdAndOrder(UUID.fromString(principalComboItem.getId()), order)
					.orElseThrow(() -> new BusinessException("Combo not found"));
		} catch (final Exception e) {
			combo = Combo.fromDTO(order, principalComboItem);
		}

		return combo;
	}

	private void validateAndPopulate(final ComboDTO comboDTO) {
		if (comboDTO == null) {
			throw new BusinessException("Combo is not valid");
		}

		if (comboDTO.getPrincipalComboItem() == null //
				|| comboDTO.getPrincipalComboItem().getProduct() == null //
				|| comboDTO.getPrincipalComboItem().getProduct().getPriceId() == 0) {
			throw new BusinessException("Principal combo item has invalid price item");
		}

		comboDTO.getPrincipalComboItem().getProduct().setPriceItem(
				this.priceService.getProductPriceById(comboDTO.getPrincipalComboItem().getProduct().getPriceId()));

		if (comboDTO.getSideComboItens() != null && !comboDTO.getSideComboItens().isEmpty()) {
			comboDTO.getSideComboItens().stream().forEach(sideComboItem -> {

				if (sideComboItem.getProduct().getPriceId() == 0) {
					throw new BusinessException("A side combo item has no price id");
				}

				sideComboItem.getProduct()
						.setPriceItem(this.priceService.getProductPriceById(sideComboItem.getProduct().getPriceId()));
			});
		}

	}

	public OrderDTO syncOrder(final Order order, final OrderDTO orderDTO) {
		if (orderDTO == null) {
			throw new BusinessException("Order is invalid");
		}

		this.removeAllCombos(order, orderDTO);

		this.synchronizeCombos(order, orderDTO);

		return this.populateOrder(order);
	}

	private void synchronizeCombos(final Order order, final OrderDTO orderDTO) {
		if (orderDTO != null && orderDTO.getCombos() != null && !orderDTO.getCombos().isEmpty()) {
			orderDTO.getCombos().stream().forEach(combo -> this.addComboItemToOrder(order, combo));
		}
	}

	private void removeAllCombos(final Order order, final OrderDTO orderDTO) {
		final List<Combo> combos = this.getComboByOrder(order);
		combos.stream().filter(combo -> combo.getPrincipalCombo() != null).forEach(this.comboRepository::delete);
		combos.stream().filter(combo -> combo.getPrincipalCombo() == null).forEach(this.comboRepository::delete);
	}

}
