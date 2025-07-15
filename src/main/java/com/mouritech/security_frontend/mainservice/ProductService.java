package com.mouritech.security_frontend.mainservice;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.mouritech.security_frontend.productdto.ProductRequest;
import com.mouritech.security_frontend.productdto.ProductResponse;

public interface ProductService {
	//ProductResponse createProduct(String productJson, Long categoryId, String sellerEmail) throws IOException;
    List<ProductResponse> getAllProducts();
    void deleteProduct(Long id, String sellerEmail);
    List<ProductResponse> getProductsByCreator(String email);
    ProductResponse getProductById(Long id);
	ProductResponse updateProduct(Long id, ProductRequest request, String sellerEmail);
	ProductResponse createProduct(ProductRequest request, Long categoryId, String sellerEmail);
}
