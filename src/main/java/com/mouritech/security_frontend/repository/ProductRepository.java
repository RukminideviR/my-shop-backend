package com.mouritech.security_frontend.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mouritech.security_frontend.mainentity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCreatedBy(String email);
    @Query("SELECT COUNT(p) FROM Product p WHERE p.createdBy = :sellerEmail")
    long countByCreatedBy(@Param("sellerEmail") String sellerEmail);
    
    
}


