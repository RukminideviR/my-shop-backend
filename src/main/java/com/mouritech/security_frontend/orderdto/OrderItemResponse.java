package com.mouritech.security_frontend.orderdto;

import java.time.LocalDateTime;

public class OrderItemResponse {
    private Long itemId; // Added
    private String productName;
    private int quantity;
    private double price;
    private String status; // Added for item status (PLACED, CANCELLED, etc)
    private LocalDateTime statusUpdatedDate;

    

   
	public LocalDateTime getStatusUpdatedDate() {
		return statusUpdatedDate;
	}

	public void setStatusUpdatedDate(LocalDateTime statusUpdatedDate) {
		this.statusUpdatedDate = statusUpdatedDate;
	}

	public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	
}
