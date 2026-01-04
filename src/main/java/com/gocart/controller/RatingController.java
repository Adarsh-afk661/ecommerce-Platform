package com.gocart.controller;

import com.gocart.entity.Rating;
import com.gocart.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings(
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) String productId) {
        
        if (userId != null && !userId.isEmpty()) {
            return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
        }
        if (productId != null && !productId.isEmpty()) {
            return ResponseEntity.ok(ratingService.getRatingsByProductId(productId));
        }
        return ResponseEntity.ok(ratingService.getAllRatings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable String id) {
        return ratingService.getRatingById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(rating));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable String id, @RequestBody Rating ratingDetails) {
        return ResponseEntity.ok(ratingService.updateRating(id, ratingDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable String id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }
}
