package com.gocart.repository;

import com.gocart.entity.Order;
import com.gocart.entity.OrderItem;
import com.gocart.entity.OrderStatus;
import com.gocart.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Data Access Object for Order operations with JDBC Transaction Management.
 * Ensures ACID properties for order placement and inventory management.
 * All order operations use @Transactional to guarantee atomicity of:
 * 1. Creating order record
 * 2. Creating order items
 * 3. Reducing product inventory
 * 
 * If any operation fails, all changes are rolled back automatically.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderDAO {

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Create order with items and reduce inventory atomically.
     * Uses @Transactional to ensure all operations succeed or fail together.
     * 
     * Transaction Management:
     * - COMMIT: All operations succeed - order created, inventory reduced
     * - ROLLBACK: Any operation fails - entire transaction rolled back
     * 
     * @param order Order to create
     * @param orderItems Items in the order with quantities
     * @return Created order
     * @throws RuntimeException if any operation fails or inventory insufficient
     */
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderWithInventoryReduction(Order order, List<OrderItem> orderItems) {
        log.info("Starting atomic transaction for order creation and inventory reduction");

        try {
            // Step 1: Validate all inventory before committing changes
            log.debug("Step 1: Validating inventory for {} items", orderItems.size());
            validateInventory(orderItems);

            // Step 2: Generate order ID if not provided
            if (order.getId() == null || order.getId().isEmpty()) {
                order.setId(UUID.randomUUID().toString());
            }
            log.debug("Order ID generated: {}", order.getId());

            // Step 3: Set default values
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.ORDER_PLACED);
            }
            if (order.getIsPaid() == null) {
                order.setIsPaid(false);
            }
            if (order.getIsCouponUsed() == null) {
                order.setIsCouponUsed(false);
            }
            if (order.getCoupon() == null) {
                order.setCoupon("{}");
            }
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            // Step 4: Persist order (required before adding order items)
            log.debug("Step 2: Persisting order entity");
            entityManager.persist(order);
            entityManager.flush(); // Ensure order ID is available
            log.info("Order persisted with ID: {}", order.getId());

            // Step 5: Create order items and reduce inventory for each
            log.debug("Step 3: Creating {} order items and reducing inventory", orderItems.size());
            for (OrderItem item : orderItems) {
                // Set order reference
                item.setOrder(order);

                // Get product and validate it still exists
                Product product = entityManager.find(Product.class, item.getProduct().getId());
                if (product == null) {
                    String msg = "Product not found: " + item.getProduct().getId();
                    log.error(msg);
                    throw new RuntimeException(msg);
                }

                // Reduce inventory atomically
                boolean inventoryReduced = product.reduceQuantity(item.getQuantity());
                if (!inventoryReduced) {
                    String msg = "Insufficient inventory for product: " + product.getId() +
                            ". Available: " + product.getQuantity() +
                            ", Requested: " + item.getQuantity();
                    log.error(msg);
                    throw new RuntimeException(msg);
                }

                log.debug("Inventory reduced for product: {}. New quantity: {}",
                        product.getId(), product.getQuantity());

                // Update product in persistence context
                entityManager.merge(product);

                // Create order item
                entityManager.persist(item);
            }

            // Step 6: Flush all changes to database
            entityManager.flush();
            log.info("All order items created and inventory reduced");

            // Step 7: Commit will happen automatically at method end
            log.info("Order creation and inventory reduction transaction completed successfully");

            return order;

        } catch (Exception e) {
            // Transaction will be rolled back automatically
            log.error("Error during order creation transaction. Rolling back all changes: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    /**
     * Create order with items (without inventory reduction) atomically.
     * Useful for testing or specific business scenarios.
     * 
     * @param order Order to create
     * @param orderItems Items in the order
     * @return Created order
     */
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderWithoutInventoryReduction(Order order, List<OrderItem> orderItems) {
        log.info("Starting transaction for order creation (without inventory reduction)");

        try {
            // Generate order ID if not provided
            if (order.getId() == null || order.getId().isEmpty()) {
                order.setId(UUID.randomUUID().toString());
            }

            // Set default values
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.ORDER_PLACED);
            }
            if (order.getIsPaid() == null) {
                order.setIsPaid(false);
            }
            if (order.getIsCouponUsed() == null) {
                order.setIsCouponUsed(false);
            }
            if (order.getCoupon() == null) {
                order.setCoupon("{}");
            }
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            // Persist order
            entityManager.persist(order);
            entityManager.flush();
            log.info("Order persisted with ID: {}", order.getId());

            // Create order items
            for (OrderItem item : orderItems) {
                item.setOrder(order);
                entityManager.persist(item);
            }

            entityManager.flush();
            log.info("All order items created successfully");

            return order;

        } catch (Exception e) {
            log.error("Error during order creation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    /**
     * Restore inventory when order is cancelled.
     * Uses transaction to ensure atomicity of all inventory updates.
     * 
     * @param orderId Order ID to cancel
     * @throws RuntimeException if order not found or inventory restoration fails
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderAndRestoreInventory(String orderId) {
        log.info("Starting transaction to cancel order and restore inventory: {}", orderId);

        try {
            // Find order
            Order order = entityManager.find(Order.class, orderId);
            if (order == null) {
                String msg = "Order not found: " + orderId;
                log.error(msg);
                throw new RuntimeException(msg);
            }

            // Check if order can be cancelled
            if (order.getStatus() == OrderStatus.DELIVERED) {
                String msg = "Cannot cancel order with status: " + order.getStatus();
                log.error(msg);
                throw new RuntimeException(msg);
            }

            // Restore inventory for all items
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Product product = entityManager.find(Product.class, item.getProduct().getId());
                    if (product != null) {
                        product.increaseQuantity(item.getQuantity());
                        entityManager.merge(product);
                        log.debug("Inventory restored for product: {}. New quantity: {}",
                                product.getId(), product.getQuantity());
                    }
                }
            }

            // Update order status to ORDER_PLACED (reset state)
            order.setStatus(OrderStatus.ORDER_PLACED);
            order.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(order);

            entityManager.flush();
            log.info("Order cancelled and inventory restored successfully");

        } catch (Exception e) {
            log.error("Error during order cancellation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to cancel order: " + e.getMessage(), e);
        }
    }

    /**
     * Validate that all products have sufficient inventory
     */
    private void validateInventory(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Product product = entityManager.find(Product.class, item.getProduct().getId());
            
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProduct().getId());
            }

            if (!product.getInStock()) {
                throw new RuntimeException("Product out of stock: " + product.getId());
            }

            if (product.getQuantity() == null || product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient inventory for product: " + product.getId() +
                        ". Available: " + (product.getQuantity() != null ? product.getQuantity() : 0) +
                        ", Requested: " + item.getQuantity());
            }
        }
    }
}
