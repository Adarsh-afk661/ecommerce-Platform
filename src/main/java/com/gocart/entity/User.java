package com.gocart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String image;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Store store;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> buyerOrders;

    @PrePersist
    public void initializeCart() {
        if (this.cart == null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.cart = mapper.writeValueAsString(new ObjectMapper().createObjectNode());
            } catch (Exception e) {
                this.cart = "{}";
            }
        }
    }
}
