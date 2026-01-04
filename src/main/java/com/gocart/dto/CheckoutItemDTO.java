package com.gocart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single checkout item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemDTO {
    private String productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String storeId;
}
