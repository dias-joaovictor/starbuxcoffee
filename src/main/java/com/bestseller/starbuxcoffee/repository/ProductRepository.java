package com.bestseller.starbuxcoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.model.Product;
import com.bestseller.starbuxcoffee.model.type.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByProductTypeAndName(ProductType productType, String name);

	@Query(value = "select product from Product product where productType = ?1 and name = ?2 and deletedAt is null")
	Product findByProductTypeAndNameNotCancelled(ProductType productType, String name);

	@Query(value = "select product from Product product where id = ?1 and deletedAt is null")
	Optional<Product> findByIdProductNotCancelled(int id);

}
