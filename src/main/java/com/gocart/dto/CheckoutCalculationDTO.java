package com.gocart.dto;

import com.gocart.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO containing complete checkout calculation details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutCalculationDTO {
    private Double subtotal;
    private Double discount;
    private Double subtotalAfterDiscount;
    private Double tax;
    private Double shipping;
    private Double total;
    private String couponCode;
    private Coupon appliedCoupon;
    private List<CheckoutItemDTO> items;

    /**
     * Format total as string with currency symbol
     */
    public String getFormattedTotal() {
        return String.format("₹%.2f", total != null ? total : 0.0);
    }

    /**
     * Format subtotal as string with currency symbol
     */
    public String getFormattedSubtotal() {
        return String.format("₹%.2f", subtotal != null ? subtotal : 0.0);
    }

    /**
     * Format discount as string with currency symbol
     */
    public String getFormattedDiscount() {
        return String.format("₹%.2f", discount != null ? discount : 0.0);
    }

    /**
     * Format tax as string with currency symbol
     */
    public String getFormattedTax() {
        return String.format("₹%.2f", tax != null ? tax : 0.0);
    }

    /**
     * Format shipping as string with currency symbol
     */
    public String getFormattedShipping() {
        if (shipping == null || shipping == 0.0) {
            return "FREE";
        }
        return String.format("₹%.2f", shipping);
    }
}
