package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.CategoryRequest;
import com.craftconnect.dto.CategoryResponse;

public interface CategoryService {
	
	
	List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CategoryRequest request);
}