package com.mouritech.security_frontend.maincontroller;

import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.mainservice.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductSearchController {

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "asc") String sort
    ) {
        return ResponseEntity.ok(productSearchService.search(keyword, category, minPrice, maxPrice, sort));
    }
}