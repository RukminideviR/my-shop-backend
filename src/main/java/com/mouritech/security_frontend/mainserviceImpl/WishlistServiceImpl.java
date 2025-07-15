package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.mainentity.WishlistItem;
import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.repository.ProductRepository;
import com.mouritech.security_frontend.repository.UserRepository;
import com.mouritech.security_frontend.repository.WishlistRepository;
import com.mouritech.security_frontend.mainservice.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addToWishlist(Long productId, String userEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WishlistItem item = new WishlistItem();
        item.setProduct(product);
        item.setUser(user);

        wishlistRepository.save(item);
    }

    @Override
    public List<ProductResponse> getWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WishlistItem> items = wishlistRepository.findByUser(user);

        return items.stream().map(item -> {
            Product product = item.getProduct();
            ProductResponse res = new ProductResponse();
            res.setProductId(product.getProductId());
            res.setProductName(product.getProductName());
            res.setProductPrice(product.getProductPrice());
            res.setProductDescription(product.getProductDescription());
            res.setCategoryName(product.getCategory().getCategoryName());
            res.setImageUrl(product.getProductImageUrl());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void removeFromWishlist(Long productId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<WishlistItem> items = wishlistRepository.findByUserAndProduct(user, product);
        if (items.isEmpty()) {
            throw new RuntimeException("Item not found in wishlist");
        }
        wishlistRepository.deleteAll(items);
    }
}
