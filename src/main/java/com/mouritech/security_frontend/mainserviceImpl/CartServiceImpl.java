package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.cartdto.CartItemRequest;
import com.mouritech.security_frontend.cartdto.CartItemResponse;
import com.mouritech.security_frontend.mainentity.CartItem;
import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.mainservice.CartService;
import com.mouritech.security_frontend.repository.CartItemRepository;
import com.mouritech.security_frontend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    
    public CartItemResponse addToCart(CartItemRequest request, String userEmail) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        // Deduct stock
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setUserEmail(userEmail);

        cartItemRepository.save(cartItem);
        return mapToResponse(cartItem);
    }

    @Override
    public List<CartItemResponse> getCartItems(String userEmail) {
        return cartItemRepository.findByUserEmail(userEmail)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void removeFromCart(Long cartItemId, String userEmail) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUserEmail().equals(userEmail)) {
            throw new SecurityException("Unauthorized");
        }
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(String userEmail) {
        List<CartItem> items = cartItemRepository.findByUserEmail(userEmail);
        cartItemRepository.deleteAll(items);
    }
    
    public void updateQuantity(Long cartItemId, int quantity, String userEmail) {
        if (quantity < 1) quantity = 1;  // prevent zero or negative quantities
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUserEmail().equals(userEmail)) {
            throw new SecurityException("Unauthorized");
        }
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    private CartItemResponse mapToResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct().getProductName());
        response.setQuantity(item.getQuantity());
        response.setProductPrice(item.getProduct().getProductPrice());  // ✅ Add this line

        response.setTotalPrice(item.getQuantity() * item.getProduct().getProductPrice());
        return response;
    }
}