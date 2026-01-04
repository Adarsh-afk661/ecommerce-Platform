package com.gocart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double discount;

    @Column(nullable = false)
    private Boolean forNewUser;

    @Column(nullable = false)
    private Boolean forMember;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
