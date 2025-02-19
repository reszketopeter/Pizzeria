package tutorial.pizzeria.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Size(message = "The description of the category cannot exceed 1000 characters!")
    private String description;

    @Column(name = "price")
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
