package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.mainservice.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@PreAuthorize("hasAuthority('USER')")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addToWishlist(@PathVariable Long productId,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.addToWishlist(productId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.getWishlist(userDetails.getUsername()));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.removeFromWishlist(productId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
