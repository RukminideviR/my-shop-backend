package com.mouritech.security_frontend.controller;

import com.mouritech.security_frontend.entity.Role;
import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.productdto.ProductRequest;
import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.mouritech.security_frontend.mainservice.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    // 🔹 Test endpoint
    @PostMapping("/testAdmin")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello from Admin Controller!");
    }

    // ✅ GET: All users (Admin only)
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // ❌ DELETE: User by ID (Admin only)
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ❌ DELETE: User by Email (Admin only)
    @DeleteMapping("/users/email")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional  // ✅ Add this
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ GET: Users by Role (ADMIN only)
    @GetMapping("/users/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userRepository.findByRole(role));
    }

    // 📦 GET: All products (Admin only)
    @GetMapping("/products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getAllProductsForAdmin() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ➕ POST: Add product as admin (must pass sellerEmail manually)
//    @PostMapping(value = "/products/category/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<ProductResponse> addProductAsAdmin(
//            @PathVariable Long categoryId,
//            @RequestPart("product") String productJson,
//            @RequestParam("sellerEmail") String sellerEmail
//    ) throws IOException {
//        return ResponseEntity.ok(productService.createProduct(productJson, categoryId, sellerEmail));
//    }
}
