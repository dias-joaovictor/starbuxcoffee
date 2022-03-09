package com.bestseller.starbuxcoffee.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bestseller.starbuxcoffee.model.Combo;
import com.bestseller.starbuxcoffee.model.Order;

@Repository
public interface ComboRepository extends JpaRepository<Combo, UUID> {

	Optional<Combo> findByIdAndOrder(UUID fromString, Order order);

	List<Combo> findAllByOrderOrderByCreatedAt(Order order);

}
