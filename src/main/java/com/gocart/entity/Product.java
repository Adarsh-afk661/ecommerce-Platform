package com.gocart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private Double price;

    @ElementCollection
    @CollectionTable(name = "product_images")
    private List<String> images;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Boolean inStock;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String storeId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeId", referencedColumnName = "id", insertable = false, updatable = false)
    private Store store;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.inStock == null) {
            this.inStock = true;
        }
        if (this.quantity == null) {
            this.quantity = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reduce product quantity by specified amount
     * @param quantityToReduce Amount to reduce
     * @return true if reduction was successful, false if insufficient stock
     */
    public boolean reduceQuantity(Integer quantityToReduce) {
        if (quantityToReduce == null || quantityToReduce <= 0) {
            return false;
        }
        if (this.quantity == null) {
            this.quantity = 0;
        }
        if (this.quantity < quantityToReduce) {
            return false;
        }
        this.quantity -= quantityToReduce;
        
        // Update inStock status based on quantity
        if (this.quantity <= 0) {
            this.inStock = false;
        }
        return true;
    }

    /**
     * Increase product quantity by specified amount
     * @param quantityToAdd Amount to add
     */
    public void increaseQuantity(Integer quantityToAdd) {
        if (quantityToAdd == null || quantityToAdd <= 0) {
            return;
        }
        if (this.quantity == null) {
            this.quantity = 0;
        }
        this.quantity += quantityToAdd;
        this.inStock = true;
    }
}

