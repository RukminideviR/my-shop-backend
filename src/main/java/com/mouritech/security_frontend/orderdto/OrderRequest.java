package com.mouritech.security_frontend.orderdto;

public class OrderRequest {
    private String shippingAddress;
    private String paymentMethod;
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
    
    

    
}