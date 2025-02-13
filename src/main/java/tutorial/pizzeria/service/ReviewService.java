package tutorial.pizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorial.pizzeria.domain.Review;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.mapper.ReviewMapper;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;
import tutorial.pizzeria.dto.outgoing.ReviewListItem;
import tutorial.pizzeria.exception.ReviewNotFoundException;
import tutorial.pizzeria.repository.ReviewRepository;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public ReviewDetails createReview(ReviewCommand command) {
        Review review = reviewMapper.dtoToEntity(command);
        reviewRepository.save(review);
        return reviewMapper.entityToDto(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Sorry, the review with this id " + id + "does not exist."));
        reviewRepository.delete(review);
    }

//    public List<ReviewListItem> getReviews(Boolean isRecommend) {
//        List<Review> reviewsByIsRecommend = reviewRepository.findReviewsByIsRecommend(isRecommend);
//        if (reviewsByIsRecommend.isEmpty()) {
//            throw new ReviewNotFoundException("Not found any review with this condition: " + isRecommend);
//        }
//        return reviewMapper.entitiesToDto(reviewsByIsRecommend);
//    }
}
