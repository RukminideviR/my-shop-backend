package com.mouritech.security_frontend.repository;

import com.mouritech.security_frontend.mainentity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
	 List<Order> findByUserEmail(String userEmail);

	    @Query("SELECT SUM(o.totalAmount) FROM Order o")
	    Double getTotalRevenue();

	    @Query("SELECT COUNT(o) FROM Order o JOIN o.items oi WHERE oi.product.createdBy = :sellerEmail")
	    long countOrdersBySeller(@Param("sellerEmail") String sellerEmail);

	    @Query("SELECT SUM(oi.price) FROM Order o JOIN o.items oi WHERE oi.product.createdBy = :sellerEmail")
	    Double getTotalRevenueBySeller(@Param("sellerEmail") String sellerEmail);

	    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);
	    
//	    @Query("SELECT SUM(i.price) FROM Order o JOIN o.items i WHERE i.status NOT IN ('CANCELLED', 'RETURNED')")
//	    Double getTotalRevenue();
}
