package com.bestseller.starbuxcoffee.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.model.Cart;
import com.bestseller.starbuxcoffee.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

	Optional<Order> findByCart(Cart cart);

}
