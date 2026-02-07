package tutorial.pizzeria.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.domain.Recommendation;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;
import tutorial.pizzeria.dto.outgoing.ReviewListItem;
import tutorial.pizzeria.dto.outgoing.ReviewListItemWithTime;
import tutorial.pizzeria.service.ReviewService;

import java.util.List;

@Tag(name = "Review API", description = "Operations related to review creation, retrieval, update and deletion")
@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

        private final ReviewService reviewService;

        public ReviewController(ReviewService reviewService) {
                this.reviewService = reviewService;
        }

        @Operation(summary = "Create a new review", description = "Create a new review for a product based on the provided data and the product ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Review successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data"),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/{productId}")
        public ResponseEntity<ReviewDetails> createReview(@Valid @RequestBody ReviewCommand command,
                        @PathVariable Long productId) {
                log.info("Create New Review");
                ReviewDetails response = reviewService.createReview(command, productId);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Get reviews by recommendation status", description = "Retrieves all reviews filtered by whether the product is recommended or not.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reviews successfully retrieved"),
                        @ApiResponse(responseCode = "400", description = "Invalid recommendation value"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/{isRecommend}")
        public ResponseEntity<List<ReviewListItem>> getReviews(@PathVariable Recommendation isRecommend) {
                log.info("Get Reviews: {}", isRecommend);
                List<ReviewListItem> reviews = reviewService.getReviews(isRecommend);
                return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

        @Operation(summary = "Get reviews in chronological order", description = "Retrieves all reviews sorted by their creation timestamp.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Chronological reviews successfully retrieved"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/chronological")
        public ResponseEntity<List<ReviewListItemWithTime>> getReviewsWithTime() {
                log.info("Get reviews By Timestamp");
                List<ReviewListItemWithTime> response = reviewService.getReviewsByChronological();
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "Delete review by ID", description = "Deletes a review from the system based on its unique ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Review successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "Review not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteReview(@PathVariable Long id) {
                log.info("Delete Review With Id: {}", id);
                reviewService.deleteReview(id);
                return new ResponseEntity<>("You successfully deleted the review!", HttpStatus.OK);
        }

}
