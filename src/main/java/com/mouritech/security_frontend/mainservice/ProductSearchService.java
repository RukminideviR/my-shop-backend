package com.mouritech.security_frontend.mainservice;

import com.mouritech.security_frontend.productdto.ProductResponse;
import java.util.List;

public interface ProductSearchService {
    List<ProductResponse> search(String keyword, String category, Double minPrice, Double maxPrice, String sort);
}