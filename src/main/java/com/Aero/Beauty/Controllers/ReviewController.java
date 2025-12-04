package com.Aero.Beauty.Controllers;

import com.Aero.Beauty.Entities.Review;
import com.Aero.Beauty.Services.ReviewService;
import com.Aero.Beauty.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setDate(review.getDate());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        return dto;
    }

    @GetMapping
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(review -> ResponseEntity.ok(toDTO(review)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public List<ReviewDTO> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
