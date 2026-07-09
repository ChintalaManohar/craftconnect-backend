package com.craftconnect.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.craftconnect.dto.ProductImageRequest;
import com.craftconnect.dto.ProductImageResponse;
import com.craftconnect.dto.ProductRequest;
import com.craftconnect.dto.ProductResponse;
import com.craftconnect.dto.ProductUpdateRequest;
import com.craftconnect.entity.Category;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.ProductImage;
import com.craftconnect.entity.Role;
import com.craftconnect.entity.User;
import com.craftconnect.repository.CategoryRepository;
import com.craftconnect.repository.ProductRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));
    }
    

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Category category =
                categoryRepository.findById(
                        request.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category Not Found"));

        User artisan = getCurrentUser();
        if (artisan.getRole() != Role.ARTISAN) {
            throw new RuntimeException(
                    "Only ARTISAN can create products");
        }

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(
                request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(
                request.getQuantity());
        
        product.setCategory(category);
        product.setArtisan(artisan);

        List<ProductImageRequest> requestedImages =
                request.getImages();

        if (requestedImages == null ||
                requestedImages.isEmpty()) {

            throw new RuntimeException(
                    "At least one product image is required");
        }


        List<ProductImage> productImages =
                new ArrayList<>();

        for (int i = 0;
                i < requestedImages.size();
                i++) {

            ProductImageRequest imageRequest =
                    requestedImages.get(i);

            ProductImage image =
                    new ProductImage();

            image.setImageUrl(
                    imageRequest.getImageUrl());

            image.setPublicId(
                    imageRequest.getPublicId());

            image.setDisplayOrder(i);

            image.setProduct(product);

            productImages.add(image);
        }

        product.setImages(productImages);

        product.setImageUrl(
                requestedImages
                        .get(0)
                        .getImageUrl());


        Product savedProduct =
                productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(
                        keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse>  getProductsByCategory(Long categoryId) {

        return productRepository
        		.findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public List<ProductResponse> getMyProducts() {

        User artisan = getCurrentUser();

        return productRepository
                .findByArtisanIdAndActiveTrue(artisan.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public void deleteProduct(Long productId) {

        User artisan = getCurrentUser();

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product Not Found"));

        if (!product.getArtisan()
                .getId()
                .equals(artisan.getId())) {

            throw new RuntimeException(
                    "You can delete only your products");
        }

        product.setActive(false);

        productRepository.save(product);
    }
    @Override
    public ProductResponse updateProduct(
            Long productId,
            ProductUpdateRequest request) {

        User artisan = getCurrentUser();

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product Not Found"));

        if (!product.getArtisan()
                .getId()
                .equals(artisan.getId())) {

            throw new RuntimeException(
                    "You can update only your products");
        }


        Category category =
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category Not Found"));


        // UPDATE NORMAL PRODUCT FIELDS

        product.setName(request.getName());

        product.setDescription(
                request.getDescription());

        product.setPrice(request.getPrice());

        product.setQuantity(
                request.getQuantity());

        product.setCategory(category);


        // VALIDATE FINAL IMAGE LIST

        List<ProductImageRequest> requestedImages =
                request.getImages();

        if (requestedImages == null ||
                requestedImages.isEmpty()) {

            throw new RuntimeException(
                    "At least one product image is required");
        }


        // REMOVE ONLY THIS PRODUCT'S OLD IMAGES

        product.getImages().clear();


        // ADD THE FINAL IMAGE LIST

        for (int i = 0;
                i < requestedImages.size();
                i++) {

            ProductImageRequest imageRequest =
                    requestedImages.get(i);

            ProductImage image =
                    new ProductImage();

            image.setImageUrl(
                    imageRequest.getImageUrl());

            image.setPublicId(
                    imageRequest.getPublicId());

            image.setDisplayOrder(i);

            image.setProduct(product);

            product.getImages().add(image);
        }


        // FIRST IMAGE = PRODUCT CARD IMAGE

        product.setImageUrl(
                requestedImages
                        .get(0)
                        .getImageUrl());


        Product savedProduct =
                productRepository.save(product);


        return mapToResponse(savedProduct);
    }
    @Override
    public ProductResponse getProductById(
            Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product Not Found"));
        
        if (!product.getActive()) {

            throw new RuntimeException(
                    "Product Not Available");
        }

        return mapToResponse(product);
    }
    
    @Override
    public List<ProductResponse> getProductsByArtisan(
            Long artisanId) {

        User artisan =
                userRepository
                        .findById(artisanId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Artisan not found"));

        if (artisan.getRole() != Role.ARTISAN) {
            throw new RuntimeException(
                    "User is not an artisan");
        }

        return productRepository
                .findByArtisanId(artisanId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {

        ProductResponse response =
                new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setImageUrl(product.getImageUrl());

        response.setCategory(
                product.getCategory().getName());

        response.setArtisanName(
                product.getArtisan().getFullName());

        response.setArtisanId(
                product.getArtisan().getId());

        response.setCreatedAt(
                product.getCreatedAt());

        List<ProductImageResponse> images =
                product.getImages() == null
                        ? List.of()
                        : product.getImages()
                                .stream()
                                .map(image ->
                                        new ProductImageResponse(
                                                image.getId(),
                                                image.getImageUrl(),
                                                image.getPublicId(),
                                                image.getDisplayOrder()
                                        ))
                                .toList();

        response.setImages(images);

        return response;
    }
}