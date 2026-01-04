package com.gocart.service;

import com.gocart.entity.Rating;
import com.gocart.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Optional<Rating> getRatingById(String id) {
        return ratingRepository.findById(id);
    }

    public List<Rating> getRatingsByUserId(String userId) {
        return ratingRepository.findByUser_Id(userId);
    }

    public List<Rating> getRatingsByProductId(String productId) {
        return ratingRepository.findByProduct_Id(productId);
    }

    public Rating createRating(Rating rating) {
        if (rating.getId() == null || rating.getId().isEmpty()) {
            rating.setId(UUID.randomUUID().toString());
        }
        if (rating.getReview() == null) {
            rating.setReview("");
        }
        if (rating.getOrderId() == null) {
            rating.setOrderId("");
        }

        // Check if rating already exists
        if (rating.getUser() != null && rating.getProduct() != null) {
            Optional<Rating> existingRating = ratingRepository.findByUser_IdAndProduct_Id(
                rating.getUser().getId(), 
                rating.getProduct().getId()
            );

            if (existingRating.isPresent()) {
                Rating existing = existingRating.get();
                existing.setRating(rating.getRating());
                existing.setReview(rating.getReview());
                existing.setOrderId(rating.getOrderId());
                return ratingRepository.save(existing);
            }
        }

        return ratingRepository.save(rating);
    }

    public Rating updateRating(String id, Rating ratingDetails) {
        return ratingRepository.findById(id).map(rating -> {
            if (ratingDetails.getRating() != null) {
                rating.setRating(ratingDetails.getRating());
            }
            if (ratingDetails.getReview() != null) {
                rating.setReview(ratingDetails.getReview());
            }
            return ratingRepository.save(rating);
        }).orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    public void deleteRating(String id) {
        ratingRepository.deleteById(id);
    }
}
