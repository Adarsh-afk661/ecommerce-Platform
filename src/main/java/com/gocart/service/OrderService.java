package com.gocart.service;

import com.gocart.entity.Order;
import com.gocart.entity.OrderItem;
import com.gocart.entity.User;
import com.gocart.repository.OrderDAO;
import com.gocart.repository.OrderRepository;
import com.gocart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing orders.
 * Uses OrderDAO for transaction management to ensure:
 * - Atomic order creation with inventory reduction
 * - Automatic rollback on failure
 * - Data consistency between orders and inventory
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDAO orderDAO;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public List<Order> getOrdersByStoreId(String storeId) {
        return orderRepository.findByStore_Id(storeId);
    }

    public List<Order> getOrdersByUserIdOrStoreOwnerId(String userId) {
        return orderRepository.findByUserIdOrStoreOwnerId(userId);
    }

    /**
     * Create order with transactional inventory management.
     * This method ensures:
     * 1. Order record is created
     * 2. Order items are added
     * 3. Product inventory is reduced
     * 
     * All operations happen atomically - if any operation fails,
     * all changes are rolled back automatically.
     * 
     * @param order Order to create
     * @param orderItems Items in the order
     * @return Created order with reduced inventory
     * @throws RuntimeException if inventory is insufficient or other errors occur
     */
    @Transactional(rollbackFor = Exception.class)
    public Order createOrderWithInventoryManagement(Order order, List<OrderItem> orderItems) {
        log.info("Creating order with inventory management for {} items", orderItems.size());

        // Ensure user exists
        if (!userRepository.existsById(order.getUserId())) {
            log.debug("User {} does not exist. Creating new user record.", order.getUserId());
            User newUser = new User();
            newUser.setId(order.getUserId());
            newUser.setName("");
            newUser.setEmail("");
            newUser.setImage("");
            newUser.setCart("{}");
            userRepository.save(newUser);
        }

        // Use OrderDAO to create order with atomic inventory reduction
        Order createdOrder = orderDAO.createOrderWithInventoryReduction(order, orderItems);
        log.info("Order created successfully with ID: {}", createdOrder.getId());

        return createdOrder;
    }

    /**
     * Create order without inventory management (legacy method).
     * Use createOrderWithInventoryManagement() for production.
     * 
     * @param order Order to create
     * @return Created order
     */
    @Deprecated
    public Order createOrder(Order order) {
        log.warn("Using deprecated createOrder() method. Please use createOrderWithInventoryManagement() instead.");
        
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
        }

        // Ensure user exists
        if (!userRepository.existsById(order.getUserId())) {
            User newUser = new User();
            newUser.setId(order.getUserId());
            newUser.setName("");
            newUser.setEmail("");
            newUser.setImage("");
            newUser.setCart("{}");
            userRepository.save(newUser);
        }

        if (order.getStatus() == null) {
            order.setStatus(com.gocart.entity.OrderStatus.ORDER_PLACED);
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

        return orderRepository.save(order);
    }

    /**
     * Update order with transaction support
     */
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrder(String id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            if (orderDetails.getTotal() != null) {
                order.setTotal(orderDetails.getTotal());
            }
            if (orderDetails.getStatus() != null) {
                order.setStatus(orderDetails.getStatus());
            }
            if (orderDetails.getIsPaid() != null) {
                order.setIsPaid(orderDetails.getIsPaid());
            }
            if (orderDetails.getPaymentMethod() != null) {
                order.setPaymentMethod(orderDetails.getPaymentMethod());
            }
            if (orderDetails.getIsCouponUsed() != null) {
                order.setIsCouponUsed(orderDetails.getIsCouponUsed());
            }
            if (orderDetails.getCoupon() != null) {
                order.setCoupon(orderDetails.getCoupon());
            }
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Cancel order and restore inventory
     * This is a transactional operation that ensures inventory is restored
     * if the order cancellation is successful.
     * 
     * @param orderId Order ID to cancel
     * @throws RuntimeException if order not found or cannot be cancelled
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderAndRestoreInventory(String orderId) {
        log.info("Cancelling order and restoring inventory for order: {}", orderId);
        orderDAO.cancelOrderAndRestoreInventory(orderId);
    }

    /**
     * Delete order (use cancelOrderAndRestoreInventory for production)
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(String id) {
        log.warn("Using deleteOrder(). Consider using cancelOrderAndRestoreInventory() to restore inventory.");
        orderRepository.deleteById(id);
    }
}

