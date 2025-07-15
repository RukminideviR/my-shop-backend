package com.mouritech.security_frontend.mainserviceImpl;

import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.repository.ProductRepository;
import com.mouritech.security_frontend.mainservice.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> search(String keyword, String category, Double minPrice, Double maxPrice, String sort) {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(p -> keyword == null || p.getProductName().toLowerCase().contains(keyword.toLowerCase()))
                .filter(p -> category == null || Objects.equals(p.getCategory().getCategoryName(), category))
                .filter(p -> minPrice == null || p.getProductPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getProductPrice() <= maxPrice)
                .sorted("desc".equalsIgnoreCase(sort) ?
                        Comparator.comparing(Product::getProductPrice).reversed() :
                        Comparator.comparing(Product::getProductPrice))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse res = new ProductResponse();
        res.setProductId(product.getProductId());
        res.setProductName(product.getProductName());
        res.setProductPrice(product.getProductPrice());
        res.setProductDescription(product.getProductDescription());
        res.setCategoryName(product.getCategory().getCategoryName());
        res.setQuantity(product.getQuantity());
        res.setImageUrl(product.getProductImageUrl());
        return res;
    }
}
