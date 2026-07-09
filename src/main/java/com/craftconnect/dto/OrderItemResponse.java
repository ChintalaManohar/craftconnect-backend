package com.craftconnect.dto;

import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
	
	 private Long productId;

	    private String productName;

	    private String imageUrl;

	    private Integer quantity;

	    private BigDecimal price;

	    private String artisanName;

}
