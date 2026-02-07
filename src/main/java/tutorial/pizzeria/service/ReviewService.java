package tutorial.pizzeria.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Recommendation;
import tutorial.pizzeria.domain.Review;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.mapper.ReviewMapper;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;
import tutorial.pizzeria.dto.outgoing.ReviewListItem;
import tutorial.pizzeria.dto.outgoing.ReviewListItemWithTime;
import tutorial.pizzeria.exception.ProductNotFoundException;
import tutorial.pizzeria.exception.ReviewNotFoundException;
import tutorial.pizzeria.repository.ProductRepository;
import tutorial.pizzeria.repository.ReviewRepository;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper,
            ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.productRepository = productRepository;
    }

    public ReviewDetails createReview(ReviewCommand command, Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("No product with this id in the database!"));
        Review review = reviewMapper.dtoToEntity(command);
        reviewRepository.save(review);
        return reviewMapper.entityToDto(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(
                        () -> new ReviewNotFoundException("Sorry, the review with this id " + id + " does not exist."));
        reviewRepository.delete(review);
    }

    public List<ReviewListItem> getReviews(Recommendation isRecommend) {
        List<ReviewListItem> reviews = reviewRepository.findReviewsByIsRecommend(isRecommend);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("Not found any review with this condition: " + isRecommend);
        } else {
            return reviewMapper.entitiesToDto(reviews);
        }
    }

    public List<ReviewListItemWithTime> getReviewsByChronological() {
        List<ReviewListItemWithTime> reviews = reviewRepository.findAllByReviewByTimestampDesc();
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("Sorry, we didn't find any reviews!");
        }
        return reviews;
    }

}
