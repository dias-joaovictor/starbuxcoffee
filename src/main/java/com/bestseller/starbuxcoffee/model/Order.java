package com.bestseller.starbuxcoffee.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.bestseller.starbuxcoffee.core.model.BaseModel;
import com.bestseller.starbuxcoffee.dto.OrderDTO;

@Entity
@Table(name = "STB_ORDER")
public class Order extends BaseModel {

	private static final long serialVersionUID = -537976388770839236L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;

	@JoinColumn(name = "cartId", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	private Cart cart;

	@Column(name = "totalPrice", nullable = false)
	private double totalPrice;

	@Column(name = "finalPrice", nullable = false)
	private double finalPrice;

	public Order() {
		super();
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public Cart getCart() {
		return this.cart;
	}

	public void setCart(final Cart cart) {
		this.cart = cart;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(final double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getFinalPrice() {
		return this.finalPrice;
	}

	public void setFinalPrice(final double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public static final Order create(final Cart cart) {
		final Order order = new Order();
		order.setCart(cart);
		return order;
	}

	public OrderDTO toDTO() {
		final OrderDTO order = new OrderDTO();
		order.setId(this.getId().toString());
		return order;
	}

}
