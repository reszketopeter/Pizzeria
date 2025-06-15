package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tutorial.pizzeria.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM order_item oi WHERE oi.order.id=:id AND oi.order.orderStatus = 'PENDING'")
    List<OrderItem> getOrderItemsWithOrderId(@Param("id") Long id);
}
