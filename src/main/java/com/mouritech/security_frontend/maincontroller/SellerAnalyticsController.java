package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.analyticsDto.SellerAnalyticsDto;
import com.mouritech.security_frontend.mainservice.OrderService;
import com.mouritech.security_frontend.mainservice.SellerAnalyticsService;
import com.mouritech.security_frontend.orderdto.OrderResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class SellerAnalyticsController {

	@Autowired
	private SellerAnalyticsService analyticsService;

	@Autowired
	private OrderService orderService;

	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping
	public ResponseEntity<SellerAnalyticsDto> getSellerAnalytics(Authentication authentication) {
		String sellerEmail = authentication.getName();
		SellerAnalyticsDto analytics = analyticsService.getAnalytics(sellerEmail);
		return ResponseEntity.ok(analytics);
	}

	@PreAuthorize("hasAuthority('SELLER')")
	@GetMapping("/orders")
	public ResponseEntity<List<OrderResponse>> getSellerOrders(@AuthenticationPrincipal UserDetails user) {
		List<OrderResponse> orders = analyticsService.getOrdersBySellerEmail(user.getUsername());
		return ResponseEntity.ok(orders);
	}

}
