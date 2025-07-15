package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.cartdto.CartItemRequest;
import com.mouritech.security_frontend.cartdto.CartItemResponse;
import com.mouritech.security_frontend.mainservice.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasAuthority('USER')")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartItemRequest request,
                                                      @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(cartService.addToCart(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(cartService.getCartItems(user.getUsername()));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartItemId,
                                           @AuthenticationPrincipal UserDetails user) {
        cartService.removeFromCart(cartItemId, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails user) {
        cartService.clearCart(user.getUsername());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{cartItemId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long cartItemId,
                                               @RequestParam int quantity,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        cartService.updateQuantity(cartItemId, quantity, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}