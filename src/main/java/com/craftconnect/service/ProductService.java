package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.ProductRequest;
import com.craftconnect.dto.ProductResponse;
import com.craftconnect.dto.ProductUpdateRequest;

public interface ProductService {

    ProductResponse createProduct(
            ProductRequest request);

    List<ProductResponse> getAllProducts();
    
    List<ProductResponse> getMyProducts();
    
    ProductResponse updateProduct(
            Long productId,
            ProductUpdateRequest request);
    
    void deleteProduct(Long productId);
    
    ProductResponse getProductById(
            Long productId);
    
    
    List<ProductResponse> searchProducts(
            String keyword);

    List<ProductResponse> getProductsByCategory(
            Long categoryId);
    
    List<ProductResponse> getProductsByArtisan(
            Long artisanId);
}