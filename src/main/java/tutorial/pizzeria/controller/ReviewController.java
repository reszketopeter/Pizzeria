package tutorial.pizzeria.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;
import tutorial.pizzeria.service.ReviewService;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDetails> createReview(@RequestBody ReviewCommand command) {
        log.info("Create New Review");
        ReviewDetails response = reviewService.createReview(command);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get method isRecommend Yes/NO
    //Get method Timestamp

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        log.info("Delete Review With Id: {}", id);
        reviewService.deleteReview(id);
        return new ResponseEntity<>("You successfully deleted the review!", HttpStatus.OK);
    }

}
