package tutorial.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tutorial.pizzeria.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findCustomerByEmail(@Param("email") String username);
}
