package com.mouritech.security_frontend.productdto;

import java.time.LocalDateTime;

public class ProductResponse {

	 private Long productId;
	    private String productName;
	    private double productPrice;
	    private String productDescription;
	    private String createdBy;
	    private String categoryName;
	    private String imageUrl;
	    private Integer quantity;
	    private String status;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
	    
	    
	public ProductResponse(Long productId, String productName, double productPrice, String productDescription,
				String createdBy, String categoryName, String imageUrl, Integer quantity, String status,
				LocalDateTime createdAt, LocalDateTime updatedAt) {
			super();
			this.productId = productId;
			this.productName = productName;
			this.productPrice = productPrice;
			this.productDescription = productDescription;
			this.createdBy = createdBy;
			this.categoryName = categoryName;
			this.imageUrl = imageUrl;
			this.quantity = quantity;
			this.status = status;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
	public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public ProductResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    


    
    
}
