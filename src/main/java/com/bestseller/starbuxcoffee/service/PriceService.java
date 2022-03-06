package com.bestseller.starbuxcoffee.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.model.Price;
import com.bestseller.starbuxcoffee.model.Product;
import com.bestseller.starbuxcoffee.repository.PriceRepository;

@Service
public class PriceService {

	@Autowired
	PriceRepository repository;

	public void createProductPrice(final Product product, final double price) {
		this.valideProduct(product);
		this.validatePrice(price);

		final List<Price> prices = this.repository.findAllValidProductPrice(product);
		prices.stream().forEach(item -> {
			item.expire();
			this.repository.save(item);
		});

		this.repository.save(Price.create(product, price));
		this.repository.flush();
	}

	public List<Price> getAllValidProductPrices() {
		return this.repository.findAllValidProductPrices();
	}

	public List<Price> getAllValidProductPricesByProductId(final int productId) {
		return this.repository.findAllValidProductPrices(productId);
	}

	private void validatePrice(final double price) {
		if (price <= 0) {
			throw new BusinessException("Price must be greater than 0");
		}

	}

	private void valideProduct(final Product product) {
		if (product == null || product.getId() == 0) {
			throw new BusinessException("Product is invalid");
		}
	}

	public void updateProductPriceIfNecessary(final Product product, final double price) {
		if (price > 0) {
			this.valideProduct(product);
			final List<Price> prices = this.repository.findAllValidProductPrice(product);
			this.fixOldPrices(prices);
			if (!prices.isEmpty()) {
				final Price priceItem = prices.get(0);
				if (priceItem.getValue() != price) {
					priceItem.expire();
					this.repository.save(priceItem);
					this.repository.save(Price.create(product, price));
					this.repository.flush();
				}
			}
		}
	}

	private void fixOldPrices(final List<Price> prices) {
		if (!prices.isEmpty() && prices.size() > 1) {

			final Iterator<Price> iterator = prices.iterator();

			boolean first = true;
			while (iterator.hasNext()) {
				final Price price = iterator.next();
				if (!first) {
					price.expire();
					this.repository.save(price);
					iterator.remove();
				}
				first = false;
			}
		}
	}

	public void deleteProductPrices(final Product product) {
		this.valideProduct(product);
		final List<Price> prices = this.repository.findAllValidProductPrice(product);
		prices.stream().forEach(price -> {
			price.expire();
			this.repository.save(price);
		});

		this.repository.flush();
	}

}
