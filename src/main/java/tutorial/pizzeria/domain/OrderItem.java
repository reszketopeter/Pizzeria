package tutorial.pizzeria.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "order_item_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private Double value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}

