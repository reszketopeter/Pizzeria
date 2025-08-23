package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Recommendation;
import tutorial.pizzeria.domain.Review;
import tutorial.pizzeria.dto.outgoing.ReviewListItem;
import tutorial.pizzeria.dto.outgoing.ReviewListItemWithTime;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new tutorial.pizzeria.dto.outgoing.ReviewListItem (r.customer.name, r.content, r.timestamp)" +
            "FROM review r WHERE r.isRecommend = :isRecommend")
    List<ReviewListItem> findReviewsByIsRecommend(@Param("isRecommend") Recommendation isRecommend);

    @Query("SELECT new tutorial.pizzeria.dto.outgoing.ReviewListItemWithTime (r.content, r.timestamp)" +
            "FROM review r ORDER BY r.timestamp DESC")
    List<ReviewListItemWithTime> findAllByReviewByTimestampDesc();
}
