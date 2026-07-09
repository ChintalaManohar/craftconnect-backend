package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.CategoryRequest;
import com.craftconnect.dto.CategoryResponse;
import com.craftconnect.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        return categoryService
                .createCategory(request);
    }
    @GetMapping
    public List<CategoryResponse>
    getAllCategories() {

        return categoryService
                .getAllCategories();
    }
}