package com.bestseller.starbuxcoffee.service;

import static java.text.MessageFormat.format;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.dto.ProductDTO;
import com.bestseller.starbuxcoffee.dto.ProductsDTO;
import com.bestseller.starbuxcoffee.model.Price;
import com.bestseller.starbuxcoffee.model.Product;
import com.bestseller.starbuxcoffee.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PriceService priceService;

	public void createProduct(final ProductDTO productDTO) {
		final Product product = Product.fromDTO(productDTO);

		if (productDTO.getPrice() <= 0) {
			throw new BusinessException("Price must be Greater than Zero");
		}

		if (product.getId() > 0) {
			throw new BusinessException("Product Already exists. Use the PUT method to update.");
		}

		if (this.productRepository.findByProductTypeAndNameNotCancelled(//
				product.getProductType(), //
				product.getName()) != null) {
			throw new BusinessException(format(//
					"Product with Type: {0} and Name: {1} already exists", //
					product.getProductType().name(), //
					product.getName()));
		}

		this.productRepository.save(product);

		this.priceService.createProductPrice(product, productDTO.getPrice());
	}

	public ProductDTO getById(final int id) {
		final List<Price> prices = this.priceService.getAllValidProductPricesByProductId(id);
		if (prices == null || prices.isEmpty()) {
			return null;
		}
		return prices.get(0).toProductDTO();
	}

	public ProductsDTO getAllValidProducts() {
		return new ProductsDTO(this.priceService//
				.getAllValidProductPrices()//
				.stream()//
				.map(Price::toProductDTO) //
				.collect(Collectors.toList()));
	}

	public void updateProduct(final ProductDTO productDTO) {

		final Product productToUpdate = Product.fromDTO(productDTO);

		final Product productFound = this.productRepository.findByIdProductNotCancelled(productDTO.getId())
				.orElseThrow(() -> new BusinessException("Product not found"));

		if (!productToUpdate.getName().equals(productFound.getName())) {
			if (this.productRepository.findByProductTypeAndName(productToUpdate.getProductType(),
					productToUpdate.getName()) != null) {
				throw new BusinessException(format(//
						"Product with Type: {0} and Name: {1} already exists", //
						productToUpdate.getProductType().name(), //
						productToUpdate.getName()));
			} else {
				productFound.setName(productToUpdate.getName());
			}
		}

		if (productToUpdate.getProductType() != productFound.getProductType()) {
			throw new BusinessException(
					"It's impossible to change the product type. You must to create a new Product.");
		}

		productFound.setDescription(productToUpdate.getDescription());
		productFound.setPriority(productToUpdate.getPriority());

		this.productRepository.save(productFound);

		this.priceService.updateProductPriceIfNecessary(productFound, productDTO.getPrice());

	}

	public void deleteProduct(final int id) {
		final Optional<Product> productFound = this.productRepository.findByIdProductNotCancelled(id);

		if (productFound.isPresent()) {
			productFound.get().cancel();
			this.priceService.deleteProductPrices(productFound.get());
			this.productRepository.save(productFound.get());
			this.productRepository.flush();
		}

	}

}
