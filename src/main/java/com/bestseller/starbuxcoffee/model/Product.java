package com.bestseller.starbuxcoffee.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.bestseller.starbuxcoffee.core.exception.BusinessException;
import com.bestseller.starbuxcoffee.core.model.BaseModel;
import com.bestseller.starbuxcoffee.dto.ProductDTO;
import com.bestseller.starbuxcoffee.model.type.ProductType;

@Entity
@Table(name = "stb_product")
public class Product extends BaseModel {

	private static final long serialVersionUID = -6434741844168013221L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "priority", nullable = false)
	private int priority;

	@Column(name = "deletedAt")
	private LocalDateTime deletedAt;

	public Product() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public ProductType getProductType() {
		return this.productType;
	}

	public void setProductType(final ProductType productType) {
		this.productType = productType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public LocalDateTime getDeletedAt() {
		return this.deletedAt;
	}

	public void setDeletedAt(final LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public static Product fromDTO(final ProductDTO productDTO) {
		validate(productDTO);
		final Product product = new Product();
		product.setId(productDTO.getId());
		product.setDescription(productDTO.getDescription());
		product.setName(productDTO.getName());
		product.setPriority(productDTO.getPriority());
		product.setProductType(ProductType.fromName(productDTO.getProductType()));
		return product;
	}

	private static void validate(final ProductDTO productDTO) {
		if (productDTO == null) {
			throw new BusinessException("Product is not valid");
		}

		if (StringUtils.isEmpty(productDTO.getDescription())) {
			throw new BusinessException("Product description is empty");
		}

		if (StringUtils.isEmpty(productDTO.getName())) {
			throw new BusinessException("Product name is empty");
		}

		if (StringUtils.isEmpty(productDTO.getProductType())
				|| ProductType.fromName(productDTO.getProductType()) == null) {
			throw new BusinessException("Product type is invalid. Valid Types: ".concat(ProductType.getValidTypes()));
		}
	}

	public ProductDTO toProductDTO() {
		final ProductDTO productDTO = new ProductDTO();
		productDTO.setId(this.getId());
		productDTO.setDescription(this.getDescription());
		productDTO.setName(this.getName());
		productDTO.setPriority(this.getPriority());
		productDTO.setProductType(this.getProductType().name());
		return productDTO;
	}

}
