package com.craftconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
	
	
	List<Product> findByArtisanId(Long artisanId);
	
	List<Product> findByNameContainingIgnoreCaseAndActiveTrue(
	        String keyword);
	
	List<Product> findByArtisanIdAndActiveTrue(Long artisanId);
	
	List<Product> findByActiveTrue();
	
	
	List<Product> findByCategoryIdAndActiveTrue(
	        Long categoryId);

    List<Product> findByCategoryId(
            Long categoryId);
    
    long countByActiveTrue();

    List<Product> findTop5ByActiveTrueOrderByCreatedAtDesc();
    
   

}