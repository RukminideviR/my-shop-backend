package com.mouritech.security_frontend.mainservice;

import java.util.List;

import com.mouritech.security_frontend.maindto.CategoryRequest;
import com.mouritech.security_frontend.maindto.CategoryResponse;

public interface CategoryService {
	CategoryResponse createCategory(CategoryRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
