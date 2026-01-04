package com.gocart.controller;

import com.gocart.dto.CheckoutCalculationDTO;
import com.gocart.dto.CheckoutItemDTO;
import com.gocart.dto.ErrorResponse;
import com.gocart.entity.Order;
import com.gocart.entity.OrderItem;
import com.gocart.service.CheckoutService;
import com.gocart.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for checkout operations.
 * Provides endpoints for:
 * - Calculating checkout totals with taxes, shipping, and discounts
 * - Placing orders with atomic inventory management
 * - Validating products and coupons
 */
@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderService orderService;

    /**
     * Calculate checkout totals with tax, shipping, and discount.
     * 
     * Request body should contain:
     * {
     *   "items": [
     *     {
     *       "productId": "prod123",
     *       "productName": "Product Name",
     *       "price": 999.99,
     *       "quantity": 2,
     *       "storeId": "store123"
     *     }
     *   ],
     *   "couponCode": "SAVE10" (optional)
     * }
     * 
     * @param items List of checkout items
     * @param couponCode Optional coupon code
     * @return Checkout calculation with all prices and totals
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateCheckout(
            @RequestBody CheckoutCalculationRequest request) {
        try {
            log.info("Calculating checkout for {} items", request.getItems().size());

            // Validate items
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "Bad Request", "Items list cannot be empty", null, System.currentTimeMillis())
                );
            }

            // Validate each item
            for (CheckoutItemDTO item : request.getItems()) {
                if (!checkoutService.validateProductAvailability(item.getProductId(), item.getQuantity())) {
                    return ResponseEntity.badRequest().body(
                            new ErrorResponse(400, "Bad Request", "Product not available or insufficient inventory: " + item.getProductId(), null, System.currentTimeMillis())
                    );
                }
            }

            // Calculate checkout
            CheckoutCalculationDTO calculation = checkoutService.calculateCheckout(
                    request.getItems(),
                    request.getCouponCode()
            );

            log.info("Checkout calculated successfully. Total: {}", calculation.getTotal());
            return ResponseEntity.ok(calculation);

        } catch (Exception e) {
            log.error("Error calculating checkout: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error", "Error calculating checkout: " + e.getMessage(), null, System.currentTimeMillis())
            );
        }
    }

    /**
     * Place order with atomic transaction management.
     * 
     * Creates order, order items, and reduces inventory atomically.
     * If any operation fails, all changes are rolled back.
     * 
     * Request body should contain:
     * {
     *   "order": {
     *     "userId": "user123",
     *     "storeId": "store123",
     *     "addressId": "addr123",
     *     "paymentMethod": "CARD",
     *     "total": 1049.99,
     *     "isCouponUsed": true,
     *     "coupon": "{...}"
     *   },
     *   "items": [
     *     {
     *       "productId": "prod123",
     *       "quantity": 2,
     *       "price": 999.99
     *     }
     *   ]
     * }
     * 
     * @param request Order placement request with order and items
     * @return Created order or error response
     */
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest request) {
        try {
            log.info("Processing order placement for user: {}", request.getOrder().getUserId());

            // Validate request
            if (request.getOrder() == null) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "Bad Request", "Order details are required", null, System.currentTimeMillis())
                );
            }

            if (request.getItems() == null || request.getItems().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new ErrorResponse(400, "Bad Request", "Order items are required", null, System.currentTimeMillis())
                );
            }

            // Validate all items before processing
            for (OrderItemRequest itemReq : request.getItems()) {
                if (!checkoutService.validateProductAvailability(itemReq.getProductId(), itemReq.getQuantity())) {
                    log.warn("Product not available or insufficient inventory: {}", itemReq.getProductId());
                    return ResponseEntity.badRequest().body(
                            new ErrorResponse(400, "Bad Request", "Product not available or insufficient inventory: " + itemReq.getProductId(), null, System.currentTimeMillis())
                    );
                }
            }

            // Create order with transactional inventory reduction
            Order order = request.getOrder();
            if (order.getId() == null || order.getId().isEmpty()) {
                order.setId(UUID.randomUUID().toString());
            }

            // Create order items with product references
            List<OrderItem> orderItems = request.getItems().stream().map(itemReq -> {
                OrderItem item = new OrderItem();
                item.setQuantity(itemReq.getQuantity());
                item.setPrice(itemReq.getPrice());

                // Set product reference (will be resolved by OrderDAO)
                com.gocart.entity.Product product = new com.gocart.entity.Product();
                product.setId(itemReq.getProductId());
                item.setProduct(product);

                return item;
            }).toList();

            // Place order with automatic inventory reduction and transaction management
            Order createdOrder = orderService.createOrderWithInventoryManagement(order, orderItems);

            log.info("Order placed successfully with ID: {}", createdOrder.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new OrderResponseDTO(createdOrder.getId(), "Order placed successfully", createdOrder.getTotal())
            );

        } catch (RuntimeException e) {
            log.warn("Order placement failed (likely inventory issue): {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(400, "Bad Request", e.getMessage(), null, System.currentTimeMillis())
            );
        } catch (Exception e) {
            log.error("Unexpected error during order placement: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error", "Error placing order: " + e.getMessage(), null, System.currentTimeMillis())
            );
        }
    }

    /**
     * Validate product availability before checkout
     */
    @GetMapping("/validate-product/{productId}")
    public ResponseEntity<?> validateProduct(
            @PathVariable String productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            boolean available = checkoutService.validateProductAvailability(productId, quantity);
            return ResponseEntity.ok(new ValidationResponse(available, 
                    available ? "Product available" : "Product not available or insufficient inventory"));
        } catch (Exception e) {
            log.error("Error validating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(500, "Internal Server Error", "Error validating product: " + e.getMessage(), null, System.currentTimeMillis())
            );
        }
    }

    /**
     * Validate coupon code
     */
    @GetMapping("/validate-coupon/{couponCode}")
    public ResponseEntity<?> validateCoupon(@PathVariable String couponCode) {
        try {
            CheckoutCalculationDTO calc = checkoutService.calculateCheckout(List.of(), couponCode);
            
            if (calc.getAppliedCoupon() != null) {
                return ResponseEntity.ok(new CouponValidationResponse(true, 
                        "Coupon valid", calc.getAppliedCoupon().getDiscount()));
            } else {
                return ResponseEntity.badRequest().body(
                        new CouponValidationResponse(false, "Coupon invalid or expired", 0.0)
                );
            }
        } catch (Exception e) {
            log.error("Error validating coupon: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    new CouponValidationResponse(false, "Error validating coupon", 0.0)
            );
        }
    }

    // ==================== DTOs ====================

    /**
     * Request body for checkout calculation
     */
    public static class CheckoutCalculationRequest {
        public List<CheckoutItemDTO> items;
        public String couponCode;

        public List<CheckoutItemDTO> getItems() {
            return items;
        }

        public void setItems(List<CheckoutItemDTO> items) {
            this.items = items;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }
    }

    /**
     * Request body for place order
     */
    public static class PlaceOrderRequest {
        public Order order;
        public List<OrderItemRequest> items;

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public List<OrderItemRequest> getItems() {
            return items;
        }

        public void setItems(List<OrderItemRequest> items) {
            this.items = items;
        }
    }

    /**
     * Order item in place order request
     */
    public static class OrderItemRequest {
        public String productId;
        public Integer quantity;
        public Double price;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    /**
     * Response DTO for order placement
     */
    public static class OrderResponseDTO {
        public String orderId;
        public String message;
        public Double total;

        public OrderResponseDTO(String orderId, String message, Double total) {
            this.orderId = orderId;
            this.message = message;
            this.total = total;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getMessage() {
            return message;
        }

        public Double getTotal() {
            return total;
        }
    }

    /**
     * Response DTO for validation
     */
    public static class ValidationResponse {
        public boolean valid;
        public String message;

        public ValidationResponse(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Response DTO for coupon validation
     */
    public static class CouponValidationResponse {
        public boolean valid;
        public String message;
        public Double discount;

        public CouponValidationResponse(boolean valid, String message, Double discount) {
            this.valid = valid;
            this.message = message;
            this.discount = discount;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public Double getDiscount() {
            return discount;
        }
    }
}
