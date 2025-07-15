package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.analyticsDto.SellerAnalyticsDto;

public interface SellerAnalyticsService {
	    SellerAnalyticsDto getAnalytics(String sellerEmail);

}
