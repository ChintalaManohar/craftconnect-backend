package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.craftconnect.dto.CategoryRequest;
import com.craftconnect.dto.CategoryResponse;
import com.craftconnect.entity.Category;
import com.craftconnect.repository.CategoryRepository;
import com.craftconnect.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(
            CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(
                request.getDescription());

        Category savedCategory =
                categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription());
    }
    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository
                .findAll()
                .stream()
                .map(category ->
                        new CategoryResponse(
                                category.getId(),
                                category.getName(),category.getDescription()))
                .toList();
    }
}