package tutorial.pizzeria.dto.mapper;

import org.springframework.stereotype.Component;
import tutorial.pizzeria.domain.Review;
import tutorial.pizzeria.dto.incoming.ReviewCommand;
import tutorial.pizzeria.dto.outgoing.ReviewDetails;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review dtoToEntity(ReviewCommand command) {

        Review review = new Review();

        review.setContent(command.getContent());
        review.setIsRecommend(command.getIsRecommend());
        review.setTimestamp(LocalDateTime.now());

        return new Review();
    }

    public ReviewDetails entityToDto(Review review) {

        ReviewDetails reviewDetails = new ReviewDetails();

        reviewDetails.setContent(review.getContent());
        reviewDetails.setIsRecommend(review.getIsRecommend());
        reviewDetails.setTimeStamp(review.getTimestamp().toString());

        return new ReviewDetails();
    }
}
