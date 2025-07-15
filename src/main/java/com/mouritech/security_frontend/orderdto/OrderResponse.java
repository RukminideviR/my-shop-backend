package com.mouritech.security_frontend.orderdto;

import java.time.LocalDateTime;
import java.util.List;

import com.mouritech.security_frontend.mainentity.PaymentStatus;

public class OrderResponse {
	private Long orderId;
	private String shippingAddress;
	private String paymentMethod;
	private LocalDateTime orderDate;
	private double totalAmount;
	private List<OrderItemResponse> items;
	private String razorpayOrderId;
	private Long paymentId;
	private PaymentStatus paymentStatus;

	// Getters and Setters for new fields
	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentStatus getPaymentStatus() {
	    return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
	    this.paymentStatus = paymentStatus;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

}