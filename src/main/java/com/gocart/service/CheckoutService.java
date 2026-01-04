package com.gocart.service;

import com.gocart.dto.CheckoutCalculationDTO;
import com.gocart.dto.CheckoutItemDTO;
import com.gocart.entity.Coupon;
import com.gocart.entity.Product;
import com.gocart.repository.CouponRepository;
import com.gocart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for handling all checkout calculations and pricing logic.
 * Moved from JSP to maintain strict MVC separation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;

    // Tax and shipping constants
    private static final double TAX_RATE = 0.05; // 5% tax
    private static final double SHIPPING_COST = 0.0; // Free shipping
    private static final double SHIPPING_THRESHOLD = 100.0; // Free shipping above this amount

    /**
     * Calculate complete checkout details including subtotal, tax, shipping, and final total
     * @param items List of items in checkout
     * @param couponCode Optional coupon code for discount
     * @return Detailed checkout calculation
     */
    public CheckoutCalculationDTO calculateCheckout(List<CheckoutItemDTO> items, String couponCode) {
        log.info("Calculating checkout for {} items with coupon: {}", items.size(), couponCode);

        CheckoutCalculationDTO calculation = new CheckoutCalculationDTO();

        // Calculate subtotal from items
        double subtotal = calculateSubtotal(items);
        calculation.setSubtotal(subtotal);

        // Apply coupon if provided
        double discount = 0.0;
        String appliedCoupon = null;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            Optional<Coupon> coupon = validateAndGetCoupon(couponCode, subtotal);
            if (coupon.isPresent()) {
                discount = calculateDiscount(subtotal, coupon.get());
                appliedCoupon = couponCode;
                calculation.setAppliedCoupon(coupon.get());
                log.info("Coupon {} applied. Discount: {}", couponCode, discount);
            } else {
                log.warn("Coupon {} validation failed", couponCode);
            }
        }
        calculation.setDiscount(discount);

        // Calculate subtotal after discount
        double subtotalAfterDiscount = subtotal - discount;
        calculation.setSubtotalAfterDiscount(subtotalAfterDiscount);

        // Calculate tax on discounted amount
        double tax = calculateTax(subtotalAfterDiscount);
        calculation.setTax(tax);

        // Calculate shipping
        double shipping = calculateShipping(subtotalAfterDiscount);
        calculation.setShipping(shipping);

        // Calculate total
        double total = subtotalAfterDiscount + tax + shipping;
        calculation.setTotal(total);

        // Store items for reference
        calculation.setItems(items);
        calculation.setCouponCode(appliedCoupon);

        log.info("Checkout calculation complete. Subtotal: {}, Discount: {}, Tax: {}, Shipping: {}, Total: {}",
                subtotal, discount, tax, shipping, total);

        return calculation;
    }

    /**
     * Calculate subtotal from list of items
     */
    private double calculateSubtotal(List<CheckoutItemDTO> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Validate coupon and check if it's applicable
     */
    private Optional<Coupon> validateAndGetCoupon(String couponCode, double subtotal) {
        Optional<Coupon> coupon = couponRepository.findByCode(couponCode);

        if (coupon.isEmpty()) {
            log.warn("Coupon not found: {}", couponCode);
            return Optional.empty();
        }

        Coupon c = coupon.get();

        // Check if coupon is expired
        if (c.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Coupon expired: {}", couponCode);
            return Optional.empty();
        }

        // Check minimum order value if needed
        if (subtotal < 1.0) { // Minimum order value check can be added
            log.warn("Subtotal below minimum for coupon: {}", couponCode);
            return Optional.empty();
        }

        log.info("Coupon validation successful: {}", couponCode);
        return coupon;
    }

    /**
     * Calculate discount amount based on coupon
     */
    private double calculateDiscount(double subtotal, Coupon coupon) {
        if (coupon.getDiscount() == null || coupon.getDiscount() <= 0) {
            return 0.0;
        }

        // If discount is percentage (0-1) or fixed amount
        double discount;
        if (coupon.getDiscount() < 1.0) {
            // Percentage discount
            discount = subtotal * coupon.getDiscount();
        } else {
            // Fixed amount discount
            discount = coupon.getDiscount();
        }

        // Ensure discount doesn't exceed subtotal
        return Math.min(discount, subtotal);
    }

    /**
     * Calculate tax on the subtotal
     */
    private double calculateTax(double subtotalAfterDiscount) {
        return Math.round(subtotalAfterDiscount * TAX_RATE * 100.0) / 100.0;
    }

    /**
     * Calculate shipping cost
     * Free shipping on orders above SHIPPING_THRESHOLD
     */
    private double calculateShipping(double subtotalAfterDiscount) {
        if (subtotalAfterDiscount >= SHIPPING_THRESHOLD) {
            return 0.0;
        }
        return SHIPPING_COST;
    }

    /**
     * Validate if product exists and is in stock
     */
    public boolean validateProductAvailability(String productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            log.warn("Product not found: {}", productId);
            return false;
        }

        Product p = product.get();

        // Check if product is in stock
        if (!p.getInStock()) {
            log.warn("Product not in stock: {}", productId);
            return false;
        }

        // Check if quantity is available
        if (p.getQuantity() != null && p.getQuantity() < quantity) {
            log.warn("Insufficient quantity for product: {}. Available: {}, Requested: {}",
                    productId, p.getQuantity(), quantity);
            return false;
        }

        return true;
    }

    /**
     * Get product price (handles any special pricing logic)
     */
    public Double getProductPrice(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(Product::getPrice).orElse(null);
    }
}
