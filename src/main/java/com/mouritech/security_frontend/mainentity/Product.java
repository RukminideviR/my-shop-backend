package com.mouritech.security_frontend.mainentity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name="product_id")
	private Long productId;

	private String productName;
	private double productPrice;
	private String productDescription;
	private Integer quantity;

	private String status;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private String createdBy;

	@Transient
	private String productImageUrl;

	public String getProductImageUrl() {
		return "http://localhost:8005/api/products/" + this.productId + "/image";
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		if (quantity == null || quantity <= 0) {
			this.status = "OUT_OF_STOCK";
		} else {
			this.status = "IN_STOCK";
		}
	}

	// constructor including all fields
	public Product() {
		super();
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public Product(Long productId, String productName, double productPrice, String productDescription, Integer quantity,
			String status, LocalDateTime createdAt, LocalDateTime updatedAt, Category category, String createdBy,
			String productImageUrl) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDescription = productDescription;
		this.quantity = quantity;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.category = category;
		this.createdBy = createdBy;
		this.productImageUrl = productImageUrl;
	}

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

}