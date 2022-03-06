package com.bestseller.starbuxcoffee.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.bestseller.starbuxcoffee.core.model.BaseModel;
import com.bestseller.starbuxcoffee.dto.CartDTO;

@Entity
@Table(name = "STB_CART")
public class Cart extends BaseModel {

	private static final long serialVersionUID = -537976388770839236L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name = "id", updatable = false, unique = true, nullable = false)
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;

	@Column(name = "customerId", nullable = false)
	private String customerId;

	@Column(name = "expiresAt", nullable = false)
	private LocalDateTime expiresAt;

	public Cart() {
		super();
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(final String customerId) {
		this.customerId = customerId;
	}

	public LocalDateTime getExpiresAt() {
		return this.expiresAt;
	}

	public void setExpiresAt(final LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public CartDTO toDTO() {
		final CartDTO dto = new CartDTO();
		dto.setCartId(this.getId().toString());
		dto.setExpiresAt(this.getExpiresAt());
		dto.setRemainingTime(Duration.between(LocalDateTime.now(), this.getExpiresAt()).toMinutes());
		return dto;
	}

}
