package com.mouritech.security_frontend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.mainentity.WishlistItem;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
    List<WishlistItem> findByUserAndProduct(User user, Product product);
}
