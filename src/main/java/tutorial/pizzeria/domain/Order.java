package tutorial.pizzeria.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name = "purchase")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "city_of_order")
    private String city;

    @OneToMany(mappedBy = "order")
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

}
