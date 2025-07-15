package com.mouritech.security_frontend.mainserviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouritech.security_frontend.mainentity.Category;
import com.mouritech.security_frontend.mainentity.Product;
import com.mouritech.security_frontend.mainservice.ProductService;
import com.mouritech.security_frontend.productdto.ProductRequest;
import com.mouritech.security_frontend.productdto.ProductResponse;
import com.mouritech.security_frontend.repository.CategoryRepository;
import com.mouritech.security_frontend.repository.ProductRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	 private final ProductRepository productRepository;
	    private final CategoryRepository categoryRepository;

	    @Autowired
	    public ProductServiceImpl(ProductRepository productRepository,
	                              CategoryRepository categoryRepository) {
	        this.productRepository = productRepository;
	        this.categoryRepository = categoryRepository;
	    }

	    @Override
	    public ProductResponse createProduct(ProductRequest request, Long categoryId, String sellerEmail) {
	        Category category = categoryRepository.findById(categoryId)
	                .orElseThrow(() -> new RuntimeException("Category not found"));

	        Product product = new Product();
	        product.setProductName(request.getProductName());
	        product.setProductPrice(request.getProductPrice());
	        product.setProductDescription(request.getProductDescription());
	        product.setCategory(category);
	        product.setCreatedBy(sellerEmail);
	        product.setQuantity(request.getQuantity());
	        product.setCreatedAt(LocalDateTime.now());
	        product.setUpdatedAt(LocalDateTime.now());

	        productRepository.save(product);
	        return mapToResponse(product);
	    }


	    @Override
	    public List<ProductResponse> getAllProducts() {
	        return productRepository.findAll()
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public List<ProductResponse> getProductsByCreator(String email) {
	        return productRepository.findByCreatedBy(email)
	                .stream()
	                .map(this::mapToResponse)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public ProductResponse getProductById(Long id) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));
	        return mapToResponse(product);
	    }

	    @Override
	    public ProductResponse updateProduct(Long id, ProductRequest request, String sellerEmail) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        // Only allow if the authenticated user is the creator
	        if (!product.getCreatedBy().equals(sellerEmail)) {
	            throw new SecurityException("Not allowed to update this product");
	        }

	        // ... update fields ...
	        product.setProductName(request.getProductName());
	        product.setProductPrice(request.getProductPrice());
	        product.setProductDescription(request.getProductDescription());
	        product.setQuantity(request.getQuantity());
	        product.setUpdatedAt(LocalDateTime.now());

	        // ... handle file if needed ...

	        productRepository.save(product);
	        return mapToResponse(product);
	    }

	    @Override
	    public void deleteProduct(Long id, String sellerEmail) {
	        Product product = productRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        if (!product.getCreatedBy().equals(sellerEmail)) {
	            throw new SecurityException("Not allowed to delete this product");
	        }

	        productRepository.delete(product);
	    }
	    

	    private ProductResponse mapToResponse(Product product) {
	        ProductResponse res = new ProductResponse();
	        res.setProductId(product.getProductId());
	        res.setProductName(product.getProductName());
	        res.setProductPrice(product.getProductPrice());
	        res.setProductDescription(product.getProductDescription());
	        res.setCategoryName(product.getCategory().getCategoryName());
	        res.setImageUrl(product.getProductImageUrl());
	        res.setCreatedBy(product.getCreatedBy());
	        res.setQuantity(product.getQuantity());
	        res.setStatus(product.getStatus());
	        res.setCreatedAt(product.getCreatedAt());
	        res.setUpdatedAt(product.getUpdatedAt());
	        return res;
	    }

}