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
import com.bestseller.starbuxcoffee.dto.ComboItemDTO;

@Entity
@Table(name = "STB_COMBO")
public class Combo extends BaseModel {

	private static final long serialVersionUID = -537976388770839236L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;

	@JoinColumn(name = "orderId", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	private Order order;

	@JoinColumn(name = "priceId", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	private Price price;

	@JoinColumn(name = "principalComboId", referencedColumnName = "id")
	@ManyToOne(optional = true)
	@Fetch(FetchMode.JOIN)
	private Combo principalCombo;

	public Combo() {
		super();
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(final Order order) {
		this.order = order;
	}

	public Price getPrice() {
		return this.price;
	}

	public void setPrice(final Price price) {
		this.price = price;
	}

	public Combo getPrincipalCombo() {
		return this.principalCombo;
	}

	public void setPrincipalCombo(final Combo principalCombo) {
		this.principalCombo = principalCombo;
	}

	public static Combo fromDTO(final Order order, final ComboItemDTO principalComboItem) {
		final Combo combo = new Combo();
		combo.setOrder(order);
		combo.setPrice(principalComboItem.getProduct().getPriceItem());
		return combo;
	}

}
