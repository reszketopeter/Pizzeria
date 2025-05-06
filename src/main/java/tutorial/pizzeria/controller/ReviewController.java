package tutorial.pizzeria.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ReviewDetails> createReview(@Valid @RequestBody ReviewCommand command,
                                                      @PathVariable Long productId) {
        log.info("Create New Review");
        ReviewDetails response = reviewService.createReview(command, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{isRecommend}")
    public ResponseEntity<List<ReviewListItem>> getReviews(@PathVariable Recommendation isRecommend) {
        log.info("Get Reviews: {}", isRecommend);
        List<ReviewListItem> reviews = reviewService.getReviews(isRecommend);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/chronological")
    public ResponseEntity<List<ReviewListItemWithTime>> getReviewsWithTime() {
        log.info("Get reviews By Timestamp");
        List<ReviewListItemWithTime> response = reviewService.getReviewsByChronological();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        log.info("Delete Review With Id: {}", id);
        reviewService.deleteReview(id);
        return new ResponseEntity<>("You successfully deleted the review!", HttpStatus.OK);
    }

}
