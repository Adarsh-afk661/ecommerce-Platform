package com.gocart.repository;

import com.gocart.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findByUser_Id(String userId);
    List<Rating> findByProduct_Id(String productId);
    Optional<Rating> findByUser_IdAndProduct_Id(String userId, String productId);
}
