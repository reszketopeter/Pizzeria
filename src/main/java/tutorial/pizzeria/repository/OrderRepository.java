package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Order;
import tutorial.pizzeria.domain.Product;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT p FROM purchase p WHERE p.customer.id=:id AND p.orderStatus='PENDING'")
    Order findByCustomerId(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.order.id=:id AND p.order.orderStatus = 'PENDING'")
    List<Product> getProductsWithOrderId(@Param("id") Long id);
}
