package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
