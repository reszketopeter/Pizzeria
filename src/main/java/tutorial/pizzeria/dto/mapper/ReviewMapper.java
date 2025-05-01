package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Review;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;
import tutorial.pizzeria.dto.outgoing.ReviewListItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

    public Review dtoToEntity(ReviewCommand command) {

        Review review = new Review();

        review.setCustomer(command.getCustomer());
        review.setContent(command.getContent());
        review.setIsRecommend(command.getIsRecommend());
        review.setTimestamp(LocalDateTime.now());

        return review;
    }

    public ReviewDetails entityToDto(Review review) {

        ReviewDetails reviewDetails = new ReviewDetails();

        reviewDetails.setContent(review.getContent());
        reviewDetails.setIsRecommend(review.getIsRecommend());
        reviewDetails.setTimeStamp(review.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return reviewDetails;
    }

//    public ReviewListItem entitiesToDto(List<ReviewListItem> reviews) {
//
//        ReviewListItem reviewListItem = new ReviewListItem();
//
//        reviewListItem.setCustomerName(reviews.stream()
//                .map();
//        reviewListItem.setContent(review.getContent());
//        reviewListItem.setTimeStamp(review.getTimestamp());
//
//        return reviewListItem;
//    }

    public List<ReviewListItem> entitiesToDto(List<ReviewListItem> reviews) {

        return reviews.stream()
                .map(review -> {
                    ReviewListItem reviewListItem = new ReviewListItem();
                    reviewListItem.setCustomerName(review.getCustomerName());
                    reviewListItem.setContent(review.getContent());
                    reviewListItem.setTimeStamp(review.getTimeStamp());
                    return reviewListItem;
                })
                .collect(Collectors.toList());
    }
}
