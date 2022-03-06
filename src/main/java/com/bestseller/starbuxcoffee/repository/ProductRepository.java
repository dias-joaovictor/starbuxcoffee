package com.bestseller.starbuxcoffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.model.Product;
import com.bestseller.starbuxcoffee.model.type.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByProductTypeAndName(ProductType productType, String name);

}
