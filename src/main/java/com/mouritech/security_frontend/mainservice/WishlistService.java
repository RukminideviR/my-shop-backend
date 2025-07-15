package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.productdto.ProductResponse;
import java.util.List;

public interface WishlistService {
    void addToWishlist(Long productId, String userEmail);
    List<ProductResponse> getWishlist(String userEmail);
    void removeFromWishlist(Long productId, String userEmail);
}
