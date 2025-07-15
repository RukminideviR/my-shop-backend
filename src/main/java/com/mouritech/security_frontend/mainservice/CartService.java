package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.cartdto.CartItemRequest;
import com.mouritech.security_frontend.cartdto.CartItemResponse;

import java.util.List;

public interface CartService {
    CartItemResponse addToCart(CartItemRequest request, String userEmail);
    List<CartItemResponse> getCartItems(String userEmail);
    void removeFromCart(Long cartItemId, String userEmail);
    void clearCart(String userEmail);
	void updateQuantity(Long cartItemId, int quantity, String userEmail);
}