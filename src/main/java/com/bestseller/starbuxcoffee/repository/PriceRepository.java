package com.bestseller.starbuxcoffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.model.Price;
import com.bestseller.starbuxcoffee.model.Product;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {

	@Query("select price from Price price where price.product = ?1 and price.expirationDate is null order by price.startValidDate desc, price.product.id asc, price.id desc")
	List<Price> findAllValidProductPrice(Product product);

	@Query("select price from Price price where price.expirationDate is null and price.product.deletedAt is null order by price.product.priority, price.product.id asc")
	List<Price> findAllValidProductPrices();

	@Query("select price from Price price where price.product.id = ?1 and price.product.deletedAt is null and price.expirationDate is null order by price.startValidDate desc, price.product.id asc")
	List<Price> findAllValidProductPrices(int productId);

}
