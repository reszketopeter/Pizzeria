package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
