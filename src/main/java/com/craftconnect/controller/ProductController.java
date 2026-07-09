package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.ProductRequest;
import com.craftconnect.dto.ProductResponse;
import com.craftconnect.dto.ProductUpdateRequest;
import com.craftconnect.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    @PostMapping("/createproduct")
    public ProductResponse createProduct(
            @Valid @RequestBody ProductRequest request) {

        return productService
                .createProduct(request);
    }

    @GetMapping("/getallproducts")
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }
    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam String keyword) {

        return productService
                .searchProducts(keyword);
    }
    @GetMapping("/category/{categoryId}")
    public List<ProductResponse>
    getProductsByCategory(
            @PathVariable Long categoryId) {

        return productService
                .getProductsByCategory(categoryId);
    }
    @GetMapping("/my-products")
    public List<ProductResponse> getMyProducts() {

        return productService.getMyProducts();
    }
    @DeleteMapping("/{productId}")
    public String deleteProduct(
            @PathVariable Long productId) {

        productService.deleteProduct(productId);

        return "Product Deleted Successfully";
    }
    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequest request) {

        return productService
                .updateProduct(productId,
                        request);
    }
    @GetMapping("/{productId}")
    public ProductResponse getProductById(
            @PathVariable Long productId) {

        return productService
                .getProductById(productId);
    }
    
    @GetMapping("/artisan/{artisanId}")
    public List<ProductResponse> getProductsByArtisan(
            @PathVariable Long artisanId) {

        return productService
                .getProductsByArtisan(artisanId);
    }
}