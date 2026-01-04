package com.gocart.repository;

import com.gocart.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    Optional<Store> findByUserId(String userId);
    Optional<Store> findByUsername(String username);
}
