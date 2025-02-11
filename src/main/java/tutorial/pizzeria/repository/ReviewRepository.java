package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
