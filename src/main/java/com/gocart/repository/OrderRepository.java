package com.gocart.repository;

import com.gocart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_Id(String userId);
    List<Order> findByStore_Id(String storeId);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId OR o.store.user.id = :userId")
    List<Order> findByUserIdOrStoreOwnerId(@Param("userId") String userId);
}
