package com.mouritech.security_frontend.analyticsDto;

public class SellerAnalyticsDto {

	 private long totalProducts;
	    private long totalOrders;
	    private double totalRevenue;

	    // Getters and setters
	    public long getTotalProducts() { return totalProducts; }
	    public void setTotalProducts(long totalProducts) { this.totalProducts = totalProducts; }

	    public long getTotalOrders() { return totalOrders; }
	    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

	    public double getTotalRevenue() { return totalRevenue; }
	    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}
