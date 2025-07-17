package com.mouritech.security_frontend.mainservice;

import java.util.List;

import com.mouritech.security_frontend.analyticsDto.SellerAnalyticsDto;
import com.mouritech.security_frontend.orderdto.OrderResponse;

public interface SellerAnalyticsService {
	    SellerAnalyticsDto getAnalytics(String sellerEmail);

		List<OrderResponse> getOrdersBySellerEmail(String sellerEmail);

}
