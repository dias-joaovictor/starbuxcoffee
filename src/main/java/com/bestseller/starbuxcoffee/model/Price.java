package com.bestseller.starbuxcoffee.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.bestseller.starbuxcoffee.core.model.BaseModel;
import com.bestseller.starbuxcoffee.dto.ProductDTO;

@Entity
@Table(name = "stb_price")
public class Price extends BaseModel {

	private static final long serialVersionUID = 7518386157666484572L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	private Product product;

	@Column(name = "value", nullable = false)
	private double value;

	@Column(name = "startValidDate", nullable = false)
	private LocalDate startValidDate;

	@Column(name = "expirationDate")
	private LocalDate expirationDate;

	public Price() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

	public LocalDate getStartValidDate() {
		return this.startValidDate;
	}

	public void setStartValidDate(final LocalDate startValidDate) {
		this.startValidDate = startValidDate;
	}

	public LocalDate getExpirationDate() {
		return this.expirationDate;
	}

	public void setExpirationDate(final LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void expire() {
		this.setExpirationDate(LocalDate.now());
	}

	public static final Price create(final Product product, final double value) {
		final Price price = new Price();
		price.setProduct(product);
		price.setValue(value);
		price.setStartValidDate(LocalDate.now());
		return price;
	}

	public ProductDTO toProductDTO() {
		final ProductDTO dto = this.getProduct().toProductDTO();
		dto.setPrice(this.getValue());
		return dto;
	}

}
