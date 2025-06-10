package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tutorial.pizzeria.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
